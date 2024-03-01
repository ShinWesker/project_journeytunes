<template>
<div class="flex flex-col p-4">
  <v-combobox
    label="Region"
    density="compact"

    :items="['Baden-Württemberg', 'Bayern', 'Berlin', 'Brandenburg', 'Bremen', 'Hamburg', 'Hessen', 'Mecklenburg-Vorpommern', 'Niedersachsen', 'Nordrhein-Westfalen', 'Rheinland-Pfalz', 'Saarland', 'Sachsen', 'Sachsen-Anhalt', 'Schleswig-Holstein', 'Thüringen']"
    @update:search="filters.region = $event"
  ></v-combobox>
  <div v-if="showFilters">

    <p class="pb-7">Price per night</p>
    <v-slider
      :min="0"
      :max="300"
      :step="5"
      thumb-label="always"
      @update:model-value="filters.pricePerNight = $event"
    ></v-slider>

    <div class="pb-4">
      <p>Stars</p>
      <v-rating
        hover
        color="orange-lighten-1"
        active-color="orange-lighten-1"
        @update:model-value="filters.stars =$event"
      ></v-rating>
    </div>


    <p>Name</p>
    <v-combobox
      @update:search="filters.name =$event"
    ></v-combobox>


  </div>
  <button @click="showFilters = !showFilters" class="mb-2">
    {{ showFilters ? 'Hide Filters' : 'Show Filters' }}
  </button>
  <button
    @click="requestHotels"
    class="bg-indigo-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
  >
    Search
  </button>
</div>



  <div class="grid grid-cols-2 md:grid-cols-4 gap-4 p-8">
    <div @click="() => openModal(hotel.id)" v-for="hotel in hotels" :key="hotel.id" class="cursor-pointer bg-white shadow rounded-xl">
      <div >
        <img :src="hotel.imageLink"
             :alt="`Hotel image for ${hotel.name}`"
             class="rounded-t-xl w-full h-48 object-cover">
      </div>
      <div class="p-3">
        <h3 class="font-semibold">{{ hotel.name }}</h3>
        <p class="mb-4">
          {{ hotel.description }}
        </p>
      </div>
    </div>
  </div>

  <!-- Modal -->
  <div v-if="isModalVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <div class="pt-4 pr-4 flex justify-end">
        <v-btn @click="closeModal">
          Close
        </v-btn>
      </div>
      <HotelDetails :hotel="selectedHotel" @close-modal="closeModal"/>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import store from '@/store';
import axiosHotelClient from "@/clients/axiosHotelClient.js";
import HotelDetails from "@/components/HotelDetails.vue";
import {VRating} from "vuetify/components";
import {useRouter} from "vue-router";

const hotels = computed(() => store.state.getHotels)
const isModalVisible = ref(false)
const selectedHotel = ref({})
const showFilters = ref(false)
const filters = {
  region: '',
  pricePerNight: '',
  stars: '',
  name: '',
}


function requestHotels() {
  store.dispatch('requestHotels', filters);
}

function getHotels() {
  store.dispatch('getHotels');
}

async function openModal(hotelId) {
  isModalVisible.value = true;
  try {
    const response = await axiosHotelClient.get(`hotel/${hotelId}`);
    selectedHotel.value = response.data;
  } catch (error) {
    console.error('There was an error fetching the hotel details:', error);
    closeModal();
  }
}


function closeModal() {
  isModalVisible.value = false;
}

onMounted(() => {
  getHotels()
})


const router = useRouter();

</script>

<style>

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  width: 70%;
  max-height: 75vh;
  overflow-y: auto;
}
</style>
