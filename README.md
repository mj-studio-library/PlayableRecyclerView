PlayableRecyclerView
[![](https://jitpack.io/v/mym0404/PlayableRecyclerView.svg)](https://jitpack.io/#mym0404/PlayableRecyclerView)
===============

* A fast, robust, extensible ```RecyclerView``` library for showing your Video. 

* Recycle ```Player``` internally for memory issue. You can set ```Player pool count``` manually. The default value is ```2```

* You can set how many videos you want to play at the same time. The default value is ```1```

* The candidate for playing is **automatically computed** based on visible size in device.

* Fully extensible. You can use any kind of ```LayoutManager``` or ```SnapHelper```

* On/Off default loading

* Pause ```Playable``` when ```PlayableTarget``` is completely hidden on the screen.

<image src="https://github.com/mym0404/PlayableRecyclerView/blob/master/Sample.gif" width=300/>

* Play your video in ```RecyclerView``` without **OutOfMemory**

<image src="https://github.com/mym0404/PlayableRecyclerView/blob/master/Sample2.gif" width=500/>


Install(Gradle)
------

In your app-level build.gradle
```groovy
repositories {
    ...
    maven { setUrl("https://jitpack.io") }
}
```

In your module-level build.gradle
```groovy
dependencies {
    ...
    implementation 'com.github.mym0404:PlayableRecyclerView:LATEST_VERSION'
}
```

Components
-----
- **Playable** : A model data class represents video(thumbnail) uri information, There is no interface/class indicating ```Playable``` instead, the library requires video/thumbnail uri in ```PlayableTarget``` instance.

- **PlayableTarget** : A target surface used to show our ```Playable```. It is usually a ```ViewHolder``` of ```RecyclerView```.

- **PlayableType** : A concept indicating the internal engine used to play video. At now, the ExoPlayer is only supported. The default is ExoPlayer.

- **PlayablePlayer** : An instance to play ```Playable``` using ```PlayableTarget```. ```PlayableTarget``` and ```PlayablePlayer``` is combined like MVP pattern.

- **PlayableManager** : The manager used to control playback of ```PlayableRecyclerView```. You can get this instance ```kotlin playableRecyclerView.manager```

- **PlayableView** : The player view used to play ```Playable```. For example, this override ```PlayerView``` of ExoPlayer.


Usage
-----

#### Activity or Fragment

```xml
<happy.mjstudio.playablerecyclerview.view.PlayableRecyclerView
    ...

    app:playable_autoplay="true"
    app:playable_loop_type="loop"
    app:playable_pause_during_invisible="true"
    app:playable_player_concurrent_max="1"
    app:playable_player_pool_count="2"
    app:playable_type="EXOPLAYER"
    app:playable_show_default_loading="true" />
```

#### RecyclerView ViewHolder itemView layout
```xml
<happy.mjstudio.playablerecyclerview.view.ExoPlayerPlayableView
    ...
                                                                
    app:resize_mode="zoom"
    app:surface_type="texture_view"
    app:use_controller="false" />
```
You need to set surface type of ```ExoPlayerPlayableView``` ```"texture_view"``` to fix bug when ```resize_mode="zoom"```

#### RecyclerView Adapter, ViewHolder

Adapter class has to override PlayableAdapter

ViewHolder class has to override PlayableTarget<Your Model Class>

```kotlin
class SampleAdapter(
    private val onItemClick: (Int) -> Unit
) : PlayableAdapter<SamplePlayable, SampleAdapter.SampleHolder>(DIFF) {

    fun submitItems(items: List<SamplePlayable>) {
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayableBinding.inflate(inflater, parent, false)

        return SampleHolder(binding)
    }

    override fun onBindViewHolder(holder: SampleHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class SampleHolder(private val binding: ItemPlayableBinding) : PlayableTarget<SamplePlayable>(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(layoutPosition)
            }
        }

        fun bind(item: SamplePlayable) {
            binding.playerView.transitionName = layoutPosition.toString()
            binding.playerView.setThumbnail(item.thumbnailUrl)
            binding.item = item
            binding.executePendingBindings()
        }

        override fun getPlayableView(): PlayableView {
            return binding.playerView
        }

        override fun onShowThumbnail() {
        }

        override fun onHideThumbnail() {
        }

        override fun onShowLoading() {
        }

        override fun onHideLoading() {
        }

        override fun getVideoUrl(): String {
            return currentList[layoutPosition].videoUrl
        }

        override fun getThumbnailUrl(): String? {
            return currentList[layoutPosition].thumbnailUrl
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SamplePlayable>() {
            override fun areItemsTheSame(oldItem: SamplePlayable, newItem: SamplePlayable): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SamplePlayable, newItem: SamplePlayable): Boolean {
                return oldItem == newItem
            }
        }
    }
}
```

#### Kotlin(Activity or Fragment)

```kotlin
with(recyclerView) {
    adapter = SampleAdapter { clickedPosition ->
        /** Switch Play/Pause state */
        when (manager.getPlaygingState(clickedPosition)) {
            PlayerState.PLAYING -> manager.pausePlayable(clickedPosition)
            else -> manager.playPlayable(clickedPosition)
        }
    }.apply {
        submitItems(sampleDatas)
    }
}
```

#### PlayableManager

You can get this instance ```kotlin playableRecyclerView.manager```

```kotlin
override fun onResume() {
    super.onResume()
    mBinding.recyclerView.manager.resumeCurrentPlayable()
}

override fun onPause() {
    super.onPause()
    mBinding.recyclerView.manager.pauseAllPlayables()
}
```



Limitations
-----------
* Tap to the full-landscape screen is not available now. It will be updated soon.
* Volume control is not available.
* Playback information within the same Playable is not saved. It will be updated in beta.
* Playable downloading locally is not supported now. It will be updated in betta.

Changelog
---------

* **1.0-alpha04**
    * Update Proguard

* **1.0-alpha03**
    * Pre release

License
-------

    Copyright 2020 - 2030 MJ Studio

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
