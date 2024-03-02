<template>
  <div class="trip-details">
    <v-card class="mb-4 text-lg">
      <v-card-text>
        <v-row>

          <v-col cols="6">
            <div class="p-2">
              <div class="text-lg font-weight-bold pb-3 text-center">{{ hotel.name }}</div>
              <div class="justify-center">
                <v-text-field
                  label="Address"
                  model-value=" "
                  readonly
                  variant="outlined"
                  density="compact"
                  class="text-lg">
                  {{ hotel.address }} </v-text-field>
                <v-text-field
                  label="Description"
                  model-value=" "
                  readonly
                  variant="outlined"
                  density="compact"
                  class="text-lg">
                  {{ hotel.description }}</v-text-field>
                <v-text-field
                  label="Price per Night"
                  model-value=" "
                  readonly
                  variant="outlined"
                  prefix="€"
                  density="compact"
                  class="text-lg">
                  {{ hotel.pricePerNight }} </v-text-field>
                <v-text-field
                  label="Email"
                  model-value=" "
                  readonly
                  variant="outlined"
                  density="compact"
                  class="text-lg">
                  {{ hotel.email }} </v-text-field>
                <v-text-field
                  label="Phone Number"
                  model-value=" "
                  readonly
                  variant="outlined"
                  density="compact"
                  class="text-lg">
                  {{ hotel.phoneNumber }}</v-text-field>
              </div>
              <div class="flex justify-center pt-6">
                <v-btn
                  color="indigo"
                  @click="dialog2=true"
                >
                  Route
                </v-btn>
              </div>
            </div>
          </v-col>

          <v-col cols="6" class="text-center">
            <div class="p-2">
              <div class="text-lg font-weight-bold pb-3 text-center">Playlist</div>
              <div class="d-flex flex-col justify-center items-center">
                <iframe
                  title="Spotify"
                  :src="tripData.playlistLink"
                  class="w-80 min-h-[375px]">
                </iframe>
              </div>

            </div>

          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-dialog v-model="dialog2">
      <div class="flex justify-center">
        <v-card class="route-map w-50" style="position: relative;">

          <Route
            :startLat="tripData.route.startLat"
            :startLng="tripData.route.startLng"
            :endLat="tripData.route.endLat"
            :endLng="tripData.route.endLng">
          </Route>

            <v-btn
              icon="mdi-close"
              style="position: absolute; top: 0; right: 0; z-index: 99999"
              @click="dialog2 = false">
            </v-btn>

        </v-card>
      </div>
    </v-dialog>


  </div>

</template>



<script setup>
import { defineProps, ref, watchEffect } from 'vue';
import Route from "@/components/Route.vue";

const props = defineProps({
  tripData: Object,
});

const hotel = ref({});
const dialog2 = ref(false)

watchEffect(() => {
  if (props.tripData && props.tripData.hotel) {
    hotel.value = props.tripData.hotel;
  } else {
    hotel.value = {};
  }
});

</script>

<style scoped>

.v-card{
  color: indigo;
}

.route-map {
  border-radius: 1em;
  border: 5px solid  #943bd2;
}


</style>
