<script>
import axios from 'axios';
export default {
    data() {
        return {
            pizze: {
                nome: "",
                descrizione: "",
                foto: "",
                prezzo: 0,
                offerte: [],
                ingredienti: []
            },
            nomePizza: "",
            nomeCreazionePizza: "",
            descrizioneCreazionePizza: "",
            fotoCreazionePizza: "",
            prezzoCreazionePizza: 0
        }
    },
    methods: {
        getPizze() {
            axios.get('http://localhost:8080/api/pizza/v1/pizze')
                .then(response => {
                    this.pizze = response.data;
                })
                .catch(error => {
                    console.error(error);
                })
        },
        filteredPizze() {
            axios.get(`http://localhost:8080/api/pizza/v1/pizze/filter?nome=${this.nomePizza}`)
                .then(response => {
                    this.pizze = response.data;
                })
                .catch(error => {
                    console.error(error);
                })
        },
        createPizza() {
            const payload = {
                nome: this.nomeCreazionePizza,
                descrizione: this.descrizioneCreazionePizza,
                foto: this.fotoCreazionePizza,
                prezzo: this.prezzoCreazionePizza,
                offerte: [],
                ingredienti: []
            };

            axios.post('http://localhost:8080/api/pizza/v1/pizze/create', payload)
                .then(response => {
                    console.log('Pizza creata con successo');
                    this.getPizze();
                })
                .catch(error => {
                    console.error(error);
                });
        }
        ,
        deletePizza(id) {
            axios.delete(`http://localhost:8080/api/pizza/v1/pizze/${id}`)
                .then(response => {

                    console.log('Pizza cancellata con successo');

                    this.getPizze();
                })
                .catch(error => {
                    console.error(error);
                });
        },
    },
    mounted() {
        this.getPizze();
    }
}

</script>

<template>
    <p>Crea una nuova pizza</p>
    <form>
        <label for="nome">Nome pizza</label>
        <input type="text" id="nome" name="nome" v-model="nomeCreazionePizza">

        <label for="descrizione">Descrizione pizza</label>
        <input type="text" id="descrizione" name="descrizione" v-model="descrizioneCreazionePizza">

        <label for="foto">Foto pizza</label>
        <input type="text" id="foto" name="foto" v-model="fotoCreazionePizza">

        <label for="prezzo">Prezzo pizza</label>
        <input type="number" id="prezzo" name="prezzo" v-model="prezzoCreazionePizza">

        <!-- Campi per offerte e ingredienti -->

        <button @click.prevent="createPizza">Crea</button>
    </form>


    <form @submit.prevent="filteredPizze()">
        <label for="nome">Nome pizza</label>
        <input type="text" id="nome" name="nome" v-model="nomePizza">
        <button>Filtra</button>
    </form>
    <div>
        <div v-for="pizza in pizze" :key="pizza.id">
            <p class="mydel">{{ pizza.nome }}</p>
            <button @click="deletePizza(pizza.id)">Cancella</button>
        </div>
    </div>
</template>

<style>
.mydel {
    display: inline;
    margin-right: 20px;
}
</style>