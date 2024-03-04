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
                        :rules="formRules.name"
                        variant="outlined"
                        density="compact"
                      ></v-text-field>

                      <p class="mb-3">Contact Data</p>
                      <v-text-field

                        v-model="hotel.email"
                        label="Hotel Email"
                        :readonly="loading"
                        :rules="formRules.email"
                        placeholder="mail@hotel.com"
                        variant="outlined"
                        density="compact"
                      ></v-text-field>
                      <v-text-field
                        class="pb-5 pt-2"
                        v-model="hotel.phoneNumber"
                        label="Phone Number"
                        :readonly="loading"
                        :rules="formRules.phoneNumber"
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
                              :rules="formRules.street"
                              variant="outlined"
                              density="compact"
                            ></v-text-field>
                          </v-col>
                          <v-col cols="12" sm="6">
                            <v-text-field
                              v-model="hotel.city"
                              label="City"
                              :readonly="loading"
                              :rules="formRules.city"
                              variant="outlined"
                              density="compact"
                            ></v-text-field>
                          </v-col>
                          <v-col cols="12" sm="6">
                            <v-text-field
                              v-model="hotel.postalcode"
                              label="Postal Code"
                              :readonly="loading"
                              :rules="formRules.postalCode"
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
                              :rules="formRules.region"
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
                        :rules="formRules.description"
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
                          @update:model-value="hotel.stars =$event"
                        ></v-rating>
                      </div>
                      <v-text-field
                        v-model="hotel.pricePerNight"
                        label="Price per Night"
                        prefix="€"
                        :readonly="loading"
                        :rules="formRules.pricePerNight"
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

                  <v-btn type="submit" @click="submitHotel" :disabled="!form" block color="indigo" class="mt-4">Submit</v-btn>
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
import {useSnackbar} from "@/components/useSnackbar";

const hotel = ref({});
const form = ref(false);
const loading = ref(false);
const hotelImage = ref(null);
const dialogVisible = ref(false);
const { showSnackbar } = useSnackbar();

const imagePreview = computed(() => {
  return hotelImage.value ? `data:image/png;base64,${hotel.value.imageData}` : null;
});


const formRules = {
  name: [v => !!v || 'Hotel name is required'],
  city: [v => !!v || 'City is required'],
  street: [v => !!v || 'Street is required'],
  description: [v => !!v || 'Description is required'],
  address: [v => !!v || 'Address is required'],
  email: [v => !!v || 'Email is required', v => /.+@.+\..+/.test(v) || 'E-mail must be valid'],
  phoneNumber: [
    v => !!v || 'Phone number is required',
    v => /^\d+$/.test(v) || 'Phone number must be numeric'
  ],
  postalCode: [
    v => !!v || 'Postalcode is required',
    v => /^\d+$/.test(v) || 'Postalcode must be numeric'
  ],
  pricePerNight: [
    v => !!v || 'Price per Night is required',
    v => /^\d+$/.test(v) || 'Price per Night must be numeric'
  ],
  region: [v => !!v || 'Region is required'],
  stars: [
    v => !!v || 'Stars are required',
    v => v >= 1 || 'The minimum number of stars is 1',
    v => v <= 5 || 'The maximum number of stars is 5',
    v => !isNaN(parseFloat(v)) && isFinite(v) || 'The value must be a number'
  ]
};




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
    showSnackbar("A field is filled incorrectly", 4000)
    console.error(error);
  } finally {
    loading.value = false;
  }
};
</script>



