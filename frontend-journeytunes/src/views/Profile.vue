<template>
  <div>
    <v-container class="flex-col items-center align-center" fluid>
      <v-card>
        <v-card-title>
          <p class="text-4xl">Profile</p>
        </v-card-title>
        <v-divider></v-divider>
        <div class="d-flex pt-4 pl-8 pb-4 align-center">
          <div class="pr-4">
            <v-avatar color="indigo" size="60" class="white--text">
              <v-icon size="40" icon="mdi-account-circle"></v-icon>
            </v-avatar>
          </div>
          <div class="ms-2 text-xl">
            <div>{{ user.name }}</div>
            <div class="caption">{{ user.email }}</div>
          </div>
        </div>
      </v-card>

      <!--
      Trips
      -->
      <div class="pt-4">
        <v-card>
          <v-card-title>Trips</v-card-title>
          <div class="flex flex-wrap pl-2">
            <div
              v-for="(trip, index) in trips"
              :key="trip.id"
              class="w-1/4 p-2"
            >
              <div
                class="h-24 w-full bg-gray-100 flex items-center justify-center cursor-pointer"
                @click="openTrip(trip.id)"
              >
                <v-card
                  class="w-full h-full d-flex items-center text-center justify-center"
                >
                  Trip {{ index + 1 }}
                </v-card>
              </div>
            </div>
          </div>
        </v-card>
      </div>


      <!--
      Hotels
      -->
      <div class="pt-4">
        <v-card>
          <v-card-title>Registered Hotels</v-card-title>
          <div class="flex flex-wrap">
            <div v-for="hotel in hotels" :key="hotel.id" class="p-2 w-1/4">
              <v-card
                class="h-40 w-full cursor-pointer overflow-hidden relative"
                @click="openHotel(hotel.id)"
                :style="`background-image: url('${hotel.imageLink}'); background-size: cover; background-position: center;`"
              >
                <div class="w-full h-full flex items-end justify-start text-left p-2 bg-gradient-to-t from-black to-transparent">
                  <span class="text-white text-lg">{{ hotel.name }}</span>
                </div>
              </v-card>
            </div>
          </div>
        </v-card>
      </div>
    </v-container>
    <v-dialog v-model="dialog" width="600px">
      <v-card>
        <v-card-title>Edit Hotel</v-card-title>
        <v-card-text>
          <v-form>
            <v-text-field v-model="formHotel.name" label="Hotel Name" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.description" label="Description" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.address" label="Address" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.email" label="Email" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.phoneNumber" label="Phone number" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.pricePerNight" label="Price per night" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.region" label="Region" outlined dense></v-text-field>
            <v-text-field v-model="formHotel.stars" label="Stars" outlined dense></v-text-field>

          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" text @click="saveHotel">Save</v-btn>
          <v-btn color="error" text @click="deleteHotel(selectedHotel.id)">Delete️</v-btn>
          <v-btn color="grey" text @click="dialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>


</template>

<script setup>
import { onMounted, ref } from 'vue';
import axiosUserClient from "@/clients/axiosUserClient";
import axiosHotelClient from "@/clients/axiosHotelClient";
import axiosTripClient from "@/clients/axiosTripClient";

const user = ref({});
const trips = ref([]);
const formHotel = ref({});
const hotels = ref([]);
const dialog = ref(false);


onMounted(async () => {
  const authToken = localStorage.getItem('authToken');
  if (authToken) {
    try {
      const response = await axiosUserClient.post('/get', { token: authToken });
      user.value = response.data;
      console.log(user.value);

      const tripsResponse = await axiosTripClient.get('/journeys');
      trips.value = tripsResponse.data;
      console.log(trips.value);

      const hotelsResponse = await axiosHotelClient.get(`owner/${localStorage.getItem('userId')}`);
      hotels.value = hotelsResponse.data;
      console.log(hotels.value);
    } catch (error) {
      console.error(error);
    }
  }
});

function openHotel(hotelId) {
  const hotel = hotels.value.find(h => h.id === hotelId);
  if (hotel) {
    formHotel.value = {
      id : hotel.id,
      name : hotel.name,
      address : hotel.address,
      description : hotel.description,
      stars : hotel.stars,
      pricePerNight : hotel.pricePerNight,
      email : hotel.email,
      region : hotel.region,
      phoneNumber : hotel.phoneNumber
    }
    dialog.value = true;
  }
}

function openTrip(tripId) {
  console.log('Opened trip ID:', tripId);
}


function saveHotel() {
  console.log('Update hotel:', formHotel.value);
  dialog.value = false;
}

function deleteHotel(hotelId) {
  console.log('Delete hotelId:', hotelId);
  dialog.value = false;
}
</script>
