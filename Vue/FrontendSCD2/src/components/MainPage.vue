<template>
  <div>
    <h1>All Posts</h1>
    <table>
      <thead>
        <tr>
          <th>Title</th>
          <th>Content</th>
          <th>User</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="post in posts" :key="post.id">
          <td>{{ post.title }}</td>
          <td>{{ post.content }}</td>
          <td>{{ post.user.name }}</td>
          <td>{{ post.status }}</td>
          <td>
            <button @click="acceptPost(post.id)">Accept</button>
            <button @click="removePost(post.id)">Remove</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const posts = ref([]);
const router = useRouter();

const fetchPosts = async () => {
  try {
    const response = await axios.get('http://localhost:8083/getAllPosts');
    posts.value = response.data;
  } catch (error) {
    console.error('Error fetching posts:', error);
  }
};

const acceptPost = async (postId) => {
  try {
    await axios.put(`http://localhost:8083/updateStatus/${postId}`, {
      status: 'PUBLISHED'
    });
    fetchPosts(); // Refresh the list
  } catch (error) {
    console.error('Failed to accept the post:', error);
  }
};

const removePost = async (postId) => {
  try {
    await axios.put(`http://localhost:8083/updateStatus/${postId}`, {
      status: 'REMOVED'
    });
    fetchPosts(); // Refresh the list
  } catch (error) {
    console.error('Failed to remove the post:', error);
  }
};

onMounted(() => {
  const user = localStorage.getItem('user');
  if (!user) {
    router.push('/'); // Redirecționează la login dacă utilizatorul nu e autentificat
  } else {
    fetchPosts();
  }
});
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

thead {
  background-color: #4CAF50;
  color: white;
}

td,
th {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

button {
  padding: 5px 10px;
  margin: 0 5px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}
</style>