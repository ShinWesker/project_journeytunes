<template>
  <div>
    <journey-tunes-title class="pt-20">

    </journey-tunes-title>
    <div class="flex justify-center pt-60">
      <v-btn
        @click="navigateToPlanning"
        size="x-large"
        color="indigo"
      >
        Plan Your Trip!
      </v-btn>
    </div>
  </div>


</template>

<script setup>
import { useRouter } from 'vue-router';
import JourneyTunesTitle from "@/components/JourneyTunesTitle.vue";
const router = useRouter();

function navigateToPlanning() {
      router.push({name: 'PlanYourTrip'})
}

</script>



