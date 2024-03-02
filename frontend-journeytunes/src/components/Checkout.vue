<template>
  <div class="p-4">

    <div>
       <p class="text-h6">Journey Information</p>
    </div>

    <div>
      <div class="grid sm:grid-cols-1 lg:grid-cols-2 m-4">
        <div class="flex justify-center">
          <img :src="hotel.imageLink" alt="{{ hotel.name }}" class="w-80 h-80 rounded-lg" />
        </div>
        <div class=" flex-col d-flex justify-center text-center">
          <p class="text-h5 pb-4"><strong>{{hotel.pricePerNight}} €</strong> night</p>
          <p class="text-h6">Description</p>
          <p class="pb-4">{{hotel.description}}</p>
          <div class="grid grid-cols-2">
            <div class="pb-2">
              <p class="text-h6">Address</p>
              <p>{{hotel.address}}</p>
            </div>
            <div>
              <p class="text-h6">{{hotel.region}}</p>
            </div>
          </div>
          <p class="pb-2 text-h6">Contact Data</p>
          <p class="pb-2">{{hotel.email}}</p>
          <p>{{hotel.phoneNumber}}</p>
        </div>


      </div>
    </div>

    <div class="p-4">
      <div>
        <p class="text-h6">Route & Playlist</p>
      </div>
      <div class="grid sm:grid-cols-1 mx-auto lg:grid-cols-2">
          <Route
            :startLat="parseFloat(userLat)"
            :startLng="parseFloat(userLng)"
            :endLat="49.142693"
            :endLng="9.210879"
          />
        <div class="d-flex flex-col justify-center items-center">
          <iframe
            title="Spotify"
            :src="selectedPlaylist"
            class="w-80 min-h-[375px]">
          </iframe>
        </div>
      </div>
    </div>

    <div class="p-8 flex justify-center">
      <v-btn @click="confirmTrip(hotel)" size="large">
        Confirm
      </v-btn>
    </div>
  </div>


    <v-dialog
      v-model="dialogVisible"
      transition="dialog-top-transition"
      width="auto"
      persistent
    >
      <v-card>
        <v-toolbar color="indigo" dark>
          <v-toolbar-title >JourneyTunes</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <div class=" text-center text-h5 pa-12 pb-4">
            Your trip has been successfully created! <br>
            🚗 🎵 🥳 </div>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn text @click="router.push('/')">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script setup>
import {ref, onMounted, onUnmounted, watch} from 'vue';
import axiosClient from "@/clients/axiosClient";
import {useRouter} from "vue-router";
import Route from "@/components/Route.vue";

const created = ref(false)
const selectedPlaylist = ref('');
const hotel = ref({});
const router = useRouter();
const userLat = ref(localStorage.getItem('userLat'));
const userLng = ref(localStorage.getItem('userLng'));
const hotelLat = ref(localStorage.getItem('hotelLat'));
const hotelLng = ref(localStorage.getItem('hotelLng'));

const dialogVisible = ref(false);

function leavePage(){
  router.push("/")
}


watch(created, (newValue) => {
  if (newValue) {
    dialogVisible.value = true;
  }
});

const updatePlaylist = () => {
  selectedPlaylist.value = localStorage.getItem('selectedPlaylist') || 'defaultPlaylistUrl';
};

const fetchHotelById = async (hotelId) => {
  try {
    const response = await axiosClient.get(`hotels/api/v1/hotel/${hotelId}`)
    hotel.value = response.data;
    console.log(hotel.value)
  } catch (error) {
    console.error("Error fetching hotel details:", error);
  }
};

watch(hotel, (newHotel) => {
  if (newHotel && newHotel.latitude && newHotel.longitude) {
    console.log('Hotel updated, update the route accordingly.');
  }
}, { deep: true });

const hotelId = localStorage.getItem('hotelId')


const handleCustomStorageChange = () => {
  updatePlaylist();
  const currentHotelId = localStorage.getItem('hotelId');
  if (hotelId !== currentHotelId) {
    fetchHotelById(currentHotelId);
  }
};

function confirmTrip(hotel) {
  const tripData = {
    startLat: parseFloat(localStorage.getItem('userLat')),
    startLng:  parseFloat(localStorage.getItem('userLng')),
    hotelId: hotel.id,
    userId: parseInt(localStorage.getItem('userId')),
    playlistLink: localStorage.getItem('selectedPlaylist')
  };

  axiosClient.post('trips/api/v1/create',tripData)
    .then(response => {
      created.value = true
      localStorage.removeItem('playlist3_name');
      localStorage.removeItem('hotelId');
      localStorage.removeItem('playlist2_name');
      localStorage.removeItem('routeDuration');
      localStorage.removeItem('playlist1');
      localStorage.removeItem('playlist2');
      localStorage.removeItem('playlist3');
      localStorage.removeItem('playlist1_name');
      localStorage.removeItem('selectedPlaylist');
    })
    .catch(error => {
      console.error('Error creating trip:', error);
    });
}

onMounted(async () => {
  updatePlaylist();
  const currentHotelId = localStorage.getItem('hotelId');
  await fetchHotelById(currentHotelId);
  window.addEventListener('localStorageChange', handleCustomStorageChange);
});

onUnmounted(() => {
  window.removeEventListener('localStorageChange', handleCustomStorageChange);
});
</script>


<style>

</style>

