<template>
  <div class="app-container">

    <el-button type="info" @click="getList">
      <i class="el-icon-refresh" />
      同步
    </el-button>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;margin-top:30px;"
    >
      <el-table-column label="ID" prop="alertId" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.serverId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Name" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.serverName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="IP" align="center" width="200px">
        <template slot-scope="{row}">
          <span>{{ row.ip }}</span>
        </template>
      </el-table-column> 
      <el-table-column label="Location" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.location }}</span>
        </template>
      </el-table-column>
      <el-table-column label="State" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.state }}</span>
        </template>
 </el-table-column>

    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="pageArgs.page"
      :limit.sync="pageArgs.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>

import { fetchAvailServer } from '@/api/server'

import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ComplexTable2',
  components: { Pagination },
  filters: {
    parseTime: parseTime

  },
  data() {
    return {
      server: {
        serverId: '',
        serverName: '',
        ip: '',
        state: '',
        location: ''
      },
      total: 0,
      list: [],
      listLoading: true,
      dialogVisible: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      tableKey: 0,
      pageArgs: {
        page: 1,
        limit: 20
      },
      dialogFormVisible: false,
      dialogStatus: ''

    }
  },
  created() {
    this.getList()
  },
  methods: {

    getList() {
      this.listLoading = true
      fetchAvailServer(this.pageArgs).then(response => {
        if (response.data.data === null) {
          this.list = []
        } else {
          this.list = response.data.data.servers
          this.total = response.data.data.total
        }
        this.listLoading = false
      })
    },
    resetServer() {
      this.server = {
        serverId: '',
        serverName: '',
        ip: '',
        state: '',
        location: ''
      }
    },

  }
}
</script>

