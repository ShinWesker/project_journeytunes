
<template>

  <div class="flex-column d-flex align-center">
    <div class="flex">
      <p class="text-deep-purple font-bold text-3xl">{{hotel.name}}</p>
      <div class="pl-4">
        <span class="text-3xl text-yellow-500" v-for="n in hotel.stars" :key="n">★</span>
      </div>
    </div>

    <div class="grid lg:grid-cols-2 sm:grid-cols-1 p-6 justify-center align-center">
      <div class="flex justify-center">
        <img :src="hotel.imageLink" height="80%" width="80%" class="rounded-2xl" :alt="hotel.name">
      </div>

      <div class=" flex-col d-flex justify-center text-center">
        <p class="text-h5 pb-4 sm:pt-8"><strong>{{hotel.pricePerNight}} €</strong> night</p>
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



        <div class="flex pt-4 justify-center">
        <v-btn
          size="large" @click="selectHotel">
          Select Hotel
        </v-btn>
      </div>
      </div>

    </div>


  </div>

</template>

<script setup>
import {inject} from 'vue';
const moveToNextStep = inject('moveToNextStep');
const props = defineProps({
  hotel: Object,
});

const emit = defineEmits(['close-modal']);

function selectHotel() {

  localStorage.setItem('hotelId', props.hotel.id)
  moveToNextStep();
  emit('close-modal');
  const event = new Event('localStorageChange');
  window.dispatchEvent(event);
}
</script>

<style scoped>

</style>
