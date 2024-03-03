<template>
  <div ref="mapContainer" style="height: 400px;"></div>
</template>

<script setup>
import { onMounted, ref, watch, reactive } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import axiosClient from "@/clients/axiosClient";
import {mdi} from "vuetify/iconsets/mdi";

const props = defineProps({
  startLat: Number,
  startLng: Number,
  endLat: Number,
  endLng: Number
});

const mapContainer = ref(null);
let map = null;
let markers = [];
let routingControl = null;

const customIcon = L.icon({
  iconUrl: 'data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'1em\' height=\'1em\' viewBox=\'0 0 24 24\'%3E%3Cpath fill=\'%23000\' d=\'M12 11.5A2.5 2.5 0 0 1 9.5 9A2.5 2.5 0 0 1 12 6.5A2.5 2.5 0 0 1 14.5 9a2.5 2.5 0 0 1-2.5 2.5M12 2a7 7 0 0 0-7 7c0 5.25 7 13 7 13s7-7.75 7-13a7 7 0 0 0-7-7\'/%3E%3C/svg%3E',
  iconSize: [40, 60],
  iconAnchor:  [20,48],
});

async function fetchRoute(startLat, startLng, endLat, endLng) {
  const response = await axiosClient.post('/routes/api/v1/create', {
    start: {
      lat: startLat.toString(),
      lng: startLng.toString()
    },
    end: {
      lat: endLat.toString(),
      lng: endLng.toString()
    }
  });
  return response.data;
}

function clearMapContents() {
  // Remove existing markers
  markers.forEach(marker => map.removeLayer(marker));
  markers = [];

  if (routingControl) {
    map.removeControl(routingControl);
    routingControl = null;
  }
}

function createMap(result) {
  if (!map) {
    map = L.map(mapContainer.value).setView([result.start.lat, result.start.lng], 10);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
  }

  clearMapContents();

  let marker1 = L.marker([result.start.lat, result.start.lng], { icon: customIcon }).addTo(map);
  let marker2 = L.marker([result.end.lat, result.end.lng], { icon: customIcon }).addTo(map);

  markers.push(marker1, marker2);

  routingControl = L.Routing.control({
    waypoints: [
      L.latLng(result.start.lat, result.start.lng),
      L.latLng(result.end.lat, result.end.lng)
    ],
    createMarker: function(i, waypoint, n) {
      return L.marker(waypoint.latLng, { icon: customIcon });
    },
    show: false
  }).addTo(map);

  map.fitBounds(L.latLngBounds([
    [result.start.lat, result.start.lng],
    [result.end.lat, result.end.lng]
  ]), {
    padding: [25, 25]
  });
}

onMounted(async () => {
  const { startLat, startLng, endLat, endLng } = props;
  try {
    const routeResult = await fetchRoute(startLat, startLng, endLat, endLng);
    if (routeResult) {
      createMap(routeResult);
    }
  } catch (error) {
    console.error('Failed to fetch route:', error);
  }
});

watch(() => [props.startLat, props.startLng, props.endLat, props.endLng], async () => {
  try {
    const routeResult = await fetchRoute(props.startLat, props.startLng, props.endLat, props.endLng);
    if (routeResult) {
      createMap(routeResult);
    }
  } catch (error) {
    console.error('Failed to fetch route:', error);
  }
}, { deep: true });
</script>



<style scoped>
@import url('https://unpkg.com/leaflet-routing-machine/dist/leaflet-routing-machine.css');
</style>
