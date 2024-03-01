<template>
  <div>
    <v-container class="flex items-center align-center" fluid>
      <v-row justify="center">
        <v-col cols="12" sm="12" md="10" lg="8">
          <v-card class="elevation-8" color="white">
            <div class="p-2">
              <div class="text-center text-indigo font-weight-bold text-2xl p-3 pt-5">
                <h1>Register Hotel</h1>
              </div>
              <v-card-text>
                <v-form v-model="form" fast-fail @submit.prevent>
                  <v-row>
                    <v-col cols="12" md="6">
                      <p class="mb-3">Hotel</p>
                      <v-text-field
                        class="pb-5"
                        v-model="hotel.name"
                        label="Hotel Name"
                        :readonly="loading"
                        :rules="[required]"
                        variant="outlined"
                        density="compact"
                      ></v-text-field>

                      <p class="mb-3">Contact Data</p>
                      <v-text-field

                        v-model="hotel.email"
                        label="Hotel Email"
                        :readonly="loading"
                        :rules="[required]"
                        placeholder="mail@hotel.com"
                        variant="outlined"
                        density="compact"
                      ></v-text-field>
                      <v-text-field
                        class="pb-5 pt-2"
                        v-model="hotel.phoneNumber"
                        label="Phone Number"
                        :readonly="loading"
                        :rules="[required, integerRule]"
                        variant="outlined"
                        density="compact"
                      ></v-text-field>

                      <p>Address</p>
                      <div class="pt-2">
                        <v-row>
                          <v-col cols="12" sm="6">
                            <v-text-field
                              v-model="hotel.street"
                              label="Street"
                              :readonly="loading"
                              :rules="[required]"
                              variant="outlined"
                              density="compact"
                            ></v-text-field>
                          </v-col>
                          <v-col cols="12" sm="6">
                            <v-text-field
                              v-model="hotel.city"
                              label="City"
                              :readonly="loading"
                              :rules="[required]"
                              variant="outlined"
                              density="compact"
                            ></v-text-field>
                          </v-col>
                          <v-col cols="12" sm="6">
                            <v-text-field
                              v-model="hotel.postalcode"
                              label="Postal Code"
                              :readonly="loading"
                              :rules="[required, integerRule]"
                              variant="outlined"
                              density="compact"
                            ></v-text-field>
                          </v-col>
                          <!--
                          REGION
                          -->
                          <v-col cols="12" sm="6">

                            <v-combobox
                              label="Region"
                              variant="outlined"
                              density="compact"
                              :items="['Baden-Württemberg', 'Bayern', 'Berlin', 'Brandenburg', 'Bremen', 'Hamburg', 'Hessen', 'Mecklenburg-Vorpommern', 'Niedersachsen', 'Nordrhein-Westfalen', 'Rheinland-Pfalz', 'Saarland', 'Sachsen', 'Sachsen-Anhalt', 'Schleswig-Holstein', 'Thüringen']"
                              @update:search="hotel.region = $event"
                            ></v-combobox>
                          </v-col>
                        </v-row>
                      </div>

                    </v-col>

                    <v-col cols="12" md="6">
                      <p class="mb-3">Additional Information</p>
                      <v-text-field
                        v-model="hotel.description"
                        label="Description"
                        :readonly="loading"
                        :rules="[required]"
                        variant="outlined"
                        density="compact">
                      </v-text-field>
                      <div class="pb-4">
                        <p class="pl-2 pb-1 text-sm text-deep-blue">Stars</p>
                        <v-rating
                          class="pl-3"
                          v-model="hotel.stars"
                          hover
                          color="orange-lighten-1"
                          active-color="orange-lighten-1"
                          @update:model-value="filters.stars =$event"
                        ></v-rating>
                      </div>
                      <v-text-field
                        v-model="hotel.pricePerNight"
                        label="Price per Night"
                        prefix="€"
                        :readonly="loading"
                        :rules="[required, integerRule]"
                        variant="outlined"
                        density="compact"
                      ></v-text-field>
                      <v-file-input
                        clearable label="Hotel Picture"
                        v-model="hotelImage"
                        variant="outlined"
                        density="compact"
                      ></v-file-input>
                      <div class="d-flex justify-center text-center">
                        <v-img
                          v-if="imagePreview"
                          :src="imagePreview"
                          class="my-4 rounded-2xl"
                          max-height="200"
                          max-width="200"
                        >
                        </v-img>
                      </div>

                    </v-col>
                  </v-row>

                  <v-btn type="submit" @click="submitHotel" block color="indigo" class="mt-4">Submit</v-btn>
                </v-form>
              </v-card-text>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

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
            Your Hotel has been successfully registered! <br>
            🏨 🫡 </div>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn text @click="router.push('/')">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<style scoped>
.v-card {
  border-radius: 1em;
  border: solid #943bd2;
}

.v-text-field{
  color:indigo;
}

.v-card-text {
  color: indigo;
}
</style>


<script setup>
import {computed, ref, watch} from 'vue';
import { VRating } from 'vuetify/components';
import axiosClient from "@/clients/axiosClient";
import router from "@/router";

const hotel = ref({});
const form = ref(false);
const loading = ref(false);
const hotelImage = ref(null);
const dialogVisible = ref(false);

const required = (v) => !!v || 'Field is required';
const integerRule = (v) => (!v || !/^\d+$/.test(v)) ? 'Field input must be digits' : true;

const imagePreview = computed(() => {
  return hotelImage.value ? `data:image/png;base64,${hotel.value.imageData}` : null;
});

watch(hotelImage, (newVal, oldVal) => {
  if (newVal && newVal.length > 0) {
    createBase64Image(newVal[0]);
  } else {
    hotel.value.imageData = null;
  }
});

const createBase64Image = (file) => {
  if (file) {
    const reader = new FileReader();
    reader.onload = (event) => {
      const base64ImageContent = event.target.result.split(',')[1];
      hotel.value.imageData = base64ImageContent;
    };
    reader.readAsDataURL(file);
  }
};


const submitHotel = async () => {
  if (!form.value) return;

  loading.value = true;
  hotel.value.creator = localStorage.getItem('userId');

  try {
    hotel.value.address = `${hotel.value.street}, ${hotel.value.postalcode} ${hotel.value.city}`
    const response = await axiosClient.post('hotels/api/v1/create', hotel.value);
    dialogVisible.value = true;
    console.log(response.data);
  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
};
</script>



