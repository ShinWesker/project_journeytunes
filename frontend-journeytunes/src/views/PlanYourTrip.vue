<template>
  <div>
    <v-container class="mx-auto">
      <v-stepper v-model="currentStep" :items="['Hotel', 'Playlist', 'Confirm']">
        <template v-slot:item.1>
          <v-card>
            <v-card-title>Select a Hotel</v-card-title>
            <planning @select-hotel="moveToNextStep"></planning>
          </v-card>
        </template>
        <template v-slot:item.2>
          <v-card>
            <v-card-title>Create Your Playlist</v-card-title>
            <spotify-selection @select-playlist="moveToNextStep" ></spotify-selection>
          </v-card>
        </template>
        <template v-slot:item.3>
          <v-card flat>
            <v-card-title>Confirm</v-card-title>
            <checkout></checkout>
          </v-card>
        </template>

        <template v-slot:actions="{ prev }">
          <div class="pl-4 pb-4">
            <v-btn text v-if="currentStep > 1" @click="prev">Previous</v-btn>
          </div>
        </template>
      </v-stepper>
    </v-container>
  </div>

</template>

<script setup>
import { ref, provide } from 'vue';
import Planning from "@/components/Planning.vue";
import SpotifySelection from "@/components/SpotifySelection.vue";
import Checkout from "@/components/Checkout.vue";

const currentStep = ref(1);

const moveToNextStep = () => {
  currentStep.value++;
};

provide('moveToNextStep', moveToNextStep);
</script>

<style scoped>
.v-card-title {
  color: indigo;
}
</style>
