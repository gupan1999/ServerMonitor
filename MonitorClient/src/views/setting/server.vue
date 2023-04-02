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
      <el-table-column label="Actions" align="center" width="230px" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button
            v-if="row.state!='离线'"
            type="primary"
            size="mini"
            @click="handle(row)"
          >
            停止
          </el-button>
          <el-button v-if="row.state!='在线'" type="success" size="mini" @click="handle(row)">
            重启
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="handleEdit"
          >
            编辑
          </el-button>
          </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="name1" :visible.sync="dialogVisible">
      <el-cascader :v-model="selected.default" :options="options" :props="{ expandTrigger: 'hover' ,multiple:true}" emit-path: false style="width:700px;"/>
              <div style="text-align:right;">
             <el-button

            type="primary"
            size="mini"
          >
           确定
          </el-button>
              </div>
    </el-dialog>
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

import { fetchAvailServer, stop, start } from '@/api/server'

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
        serverId: 0,
        serverName: '',
        ip: '',
        state: '',
        location: ''
      },
      name1: '监控项',
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
      dialogStatus: '',
      options: [
        { label: 'system', value: 1 },
        { label: 'cpu', value: 2 },
        { label: 'disk', value: 3 },
        { label: 'diskio', value: 4 },
        { label: 'mem', value: 5 },
        { label: 'net', value: 6 },
        { label: 'netstat', value: 7 },
        { label: 'processes', value: 8 },
        { label: 'procstat', value: 9 },
        { label: 'procstat_lookup', value: 10 },
        { label: 'swap', value: 11 }
      ],
      selected: {
        default: ['0']
      }

    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleEdit() {
      this.dialogVisible = true
    },
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
        serverId: 0,
        serverName: '',
        ip: '',
        state: '',
        location: ''
      }
    },
    handle(row) {
      if (row.state === '在线') {
        stop({ 'serverId': row.serverId }).then(response => {
          // this.$notify({
          //   title: '完成',
          //   message: '停止成功',
          //   type: 'success',
          //   duration: 2000
          // })
          this.getList()
        })
      }
      if (row.state === '离线') {
        start({ 'serverId': row.serverId }).then(response => {
          // this.$notify({
          //   title: '完成',
          //   message: '启动成功',
          //   type: 'success',
          //   duration: 2000
          // })
          this.getList()
        })
      }
    }

  }
}
</script>
