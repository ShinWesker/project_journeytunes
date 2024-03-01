<script setup>
import { onMounted, ref } from 'vue'
import axiosSpotifyClient from "@/clients/axiosSpotifyClient";
import Playlists from "@/components/Playlists.vue";

const categories = ref([]);
const selectedCategories = ref([]);
const showPlaylists = ref(false)

onMounted(() => {
  useSpotifyCategories();
})

function moveForward(){
  localStorage.setItem('playlist1', selectedCategories.value[0].id)
  localStorage.setItem('playlist1_name', selectedCategories.value[0].name)
  localStorage.setItem('playlist2', selectedCategories.value[1].id)
  localStorage.setItem('playlist2_name', selectedCategories.value[1].name)
  localStorage.setItem('playlist3', selectedCategories.value[2].id)
  localStorage.setItem('playlist3_name', selectedCategories.value[2].name)
  showPlaylists.value = !showPlaylists.value

  if (!showPlaylists.value) {
    localStorage.removeItem('playlist1')
    localStorage.removeItem('playlist2')
    localStorage.removeItem('playlist3')
    localStorage.removeItem('playlist1_name')
    localStorage.removeItem('playlist2_name')
    localStorage.removeItem('playlist3_name')
  }
  }

function useSpotifyCategories() {
  axiosSpotifyClient.get("/categories")
    .then(response => {
      categories.value = response.data;
    })
    .catch(err => {
      console.log("Error fetching Spotify categories:", err);
    })
}

function toggleCategory(category) {
  const index = selectedCategories.value.indexOf(category);
  if (index > -1) {
    selectedCategories.value.splice(index, 1);
  } else {
    selectedCategories.value.push(category);
  }
}

</script>

<template>
  <div v-if="!showPlaylists" class="flex flex-col items-center justify-center">
    <v-item-group v-model="selectedCategories" multiple>
      <v-container>
        <v-row>
          <v-col
            v-for="category in categories"
            :key="category"
            lg="2"
            md="20"
          >
            <v-item>
              <v-card
                :color="selectedCategories.includes(category) ? 'primary' : ''"
                class="d-flex align-center justify-center"
                dark
                @click.prevent="() => toggleCategory(category)"
                height="80"
              >
                <v-scroll-y-transition>
                  <div class="flex-grow-1 text-center" :class="{ 'text-white': selectedCategories.includes(category), 'text-light': !selectedCategories.includes(category) }">
                    {{ category.name }}
                  </div>
                </v-scroll-y-transition>
              </v-card>
            </v-item>
          </v-col>
        </v-row>
      </v-container>
    </v-item-group>
  </div>

  <playlists v-if="showPlaylists"> </playlists>

  <div class=" flex justify-center pb-8">
    <v-btn @click="moveForward" size="large">
      {{ showPlaylists ? 'Go Back' : 'Continue' }}
    </v-btn>
  </div>
</template>



<style scoped>
.text-light {
  color: #999999;
}
</style>
