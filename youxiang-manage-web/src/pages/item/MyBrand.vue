<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

  <v-card>
    <v-card-title>
      <v-btn color="primary">新增品牌</v-btn>
      <v-spacer></v-spacer>
      <v-flex xs3>
        <v-text-field
          label="输入关键字搜索"
          v-model.lazy="search"
          append-icon="search"
        ></v-text-field>
      </v-flex>
    </v-card-title>
    <!-- 分割线 -->
    <v-divider/>
    <v-data-table
      :headers="headers"
      :items="brands"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >
      <template v-slot:items="props">
        <td class="text-xs-right">{{ props.item.id }}</td>
        <td class="text-xs-right">{{ props.item.name }}</td>
        <td class="text-xs-center"><img :src="props.item.image" /></td>
        <td class="text-xs-right">{{ props.item.letter }}</td>
        <td class="justify-center layout px-0">
          <v-btn color="info">编辑</v-btn>
          <v-btn color="warning">删除</v-btn>
        </td>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
  export default {
    data () {
      return {
        search:'',
        brands:[],
        totalBrands:0,
        loading: true,
        pagination: {},
        headers: [
          {text: 'id', align: 'center', value: 'id'},
          {text: '名称', align: 'center', sortable: false, value: 'name'},
          {text: 'LOGO', align: 'center', sortable: false, value: 'image'},
          {text: '首字母', align: 'center', value: 'letter', sortable: true,},
          {text: '操作', align: 'center', value: 'id', sortable: false}
        ]
      }
    },
    watch: {
      pagination: {
        handler () {
          this.getDataFromServer();
        },
        deep: true
      },
      search:{
        handler() {
          this.getDataFromServer();
        }
      }
    },
    mounted () {
      this.getDataFromServer();
    },
    methods: {
      getDataFromServer(){
        this.$http.get("/item/brand/page",{
          params:{
            key:this.search,
            page:this.pagination.page,
            row:this.pagination.rowsPerPage,
            sortBy:this.pagination.sortBy,
            desc:this.pagination.descending,
          }
        }).then(resp => {
          console.log(resp);
          this.brands = resp.data.items;
          this.totalBrands = resp.data.total;
          this.loading = false;
        })
      }
    }
  }
</script>
