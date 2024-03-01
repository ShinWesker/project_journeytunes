<script setup>
import { ref, inject } from 'vue'
import axiosClient from "@/clients/axiosClient";
const playlistCodes = ref([]);
const categories = ref(['', '', '']);
const selectedPlaylistIndex = ref(-1);


const moveToNextStep = inject('moveToNextStep');


function selectPlaylist(index, code) {
  selectedPlaylistIndex.value = index;
  const oldValue = localStorage.getItem('selectedPlaylist');
  localStorage.setItem('selectedPlaylist', code);
  if (oldValue !== code) {
    const event = new Event('localStorageChange');
    window.dispatchEvent(event);
  }
  moveToNextStep();
}

function getPlaylistCode(categoryName, index) {
  const category = localStorage.getItem(categoryName);
  if (category) {
    categories.value[index] = category;
    updateCategoryNames()
    axiosClient.get(`spotify/api/v1/playlist?category=${category}`)
      .then(response => {
        const url = Object.keys(response.data)[0];
        const code = url.split('/').pop();
        playlistCodes.value[index] = `https://open.spotify.com/embed/playlist/${code}`;
      })
      .catch(err => {
        console.log("Error fetching Spotify categories:", err);
      });
  } else {
    console.log(`Missing data for ${categoryName} or routeDuration in localStorage.`);
  }
}

function updateCategoryNames() {
  categories.value[0] = localStorage.getItem('playlist1_name') || 'Playlist 1';
  categories.value[1] = localStorage.getItem('playlist2_name') || 'Playlist 2';
  categories.value[2] = localStorage.getItem('playlist3_name') || 'Playlist 3';
}

getPlaylistCode('playlist1', 0);
getPlaylistCode('playlist2', 1);
getPlaylistCode('playlist3', 2);

</script>

<template>
  <div class="flex-col">
    <div class="grid sm:grid-cols-1 lg:grid-cols-3 md:grid-cols-3 gap-4">
      <div v-for="(src, index) in playlistCodes" :key="index" class="flex justify-center">
        <div class="flex flex-col items-center">
          <p class="text-h5 pb-6 pt-4 font-bold text-deep-purple" v-if="categories[index]">{{ categories[index]}}</p>

          <div >
            <iframe
              v-if="src"
              :title="'Playlist ' + (index +1)"
              :src="src"
              class="w-100 h-[400px]"
              >
            </iframe>

          </div>
          <div v-if="!src">Loading...</div>
          <div class="pt-6 pb-6">
            <v-btn @click="selectPlaylist(index, src)" size="large" class="text-2xl">
              Select
            </v-btn>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

