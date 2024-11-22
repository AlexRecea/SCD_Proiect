<template>
  <div>
    <!-- Buton pentru afișarea/ascunderea tabelului -->
    <button @click="toggleTable">
      {{ showTable ? 'Hide All Posts' : 'Show All Posts' }}
    </button>

    <!-- Indicator de încărcare -->
    <p v-if="isLoading">Loading...</p>

    <!-- Mesaj de eroare -->
    <p v-if="error" style="color: red;">{{ error }}</p>

    <!-- Buton pentru reîncărcarea datelor -->
    <button v-if="showTable && !isLoading" @click="getPosts">Reload Posts</button>

    <!-- Tabelul va fi afișat doar dacă showTable este true -->
    <table v-if="showTable && !isLoading">
      <thead>
        <tr>
          <th>Title</th>
          <th>Content</th>
          <th>Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="post in allPosts" :key="post.id">
          <td>{{ post.title }}</td>
          <td>{{ post.content }}</td>
          <td>
            {{ new Date(post.created_on).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }) }}
            {{ new Date(post.created_on).toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' }) }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

// Definirea tipului Post
interface Post {
  id: number;
  title: string;
  content: string;
  user_id: string;
  created_on: string;
}

// Variabile reactive pentru date și starea aplicației
const allPosts = ref<Post[]>([])
const showTable = ref(false)
const isLoading = ref(false)
const error = ref<string | null>(null)

// Funcția pentru a obține postările de la server
const getPosts = async () => {
  isLoading.value = true
  error.value = null
  try {
    const response = await axios.get('http://localhost:8083/getAllPosts')
    allPosts.value = response.data
  } catch (err) {
    error.value = 'Failed to fetch posts. Please try again later.'
  } finally {
    isLoading.value = false
  }
}

// Funcția pentru afișarea/ascunderea tabelului
const toggleTable = () => {
  if (!showTable.value) {
    getPosts()
  }
  showTable.value = !showTable.value
}
</script>

<style scoped>
/* Stiluri pentru butoane */
button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 20px;
}

button:hover {
  background-color: #45a049;
}

/* Stiluri pentru tabel */
table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  font-size: 16px;
  text-align: left;
}

th {
  background-color: #4CAF50;
  color: white;
  padding: 12px;
  border: 1px solid #ddd;
}

td {
  padding: 12px;
  border: 1px solid #ddd;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #e2e2e2;
}

/* Stiluri pentru mesajele de eroare */
p {
  font-size: 16px;
}
</style>