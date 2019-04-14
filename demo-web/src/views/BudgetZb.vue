<comment>
    # 组件注释
</comment>
<template>
  <div class="layout">
    <Modal v-model="showAddModal" :title="modalTitle" draggable footer-hide @addBudgetZb="addBudgetZb" @closeModal="closeModal">
      <BudgetZbEdit :doEdit="this.pageState === 'Modify'"></BudgetZbEdit>
    </Modal>
    <Modal v-model="showEditModal" :title="modalTitle" draggable footer-hide @editBudgetZb="editBudgetZb" @closeModal="closeModal">
      <BudgetZbEdit :budgetZb="Object.assign({}, this.currentSelecZb)" :doEdit="this.pageState === 'Modify'"></BudgetZbEdit>
    </Modal>
    <Layout>
      <Header>
        <Menu  ref="menu" mode="horizontal" theme="dark" @on-select="menuSelect">
          <div class="layout-nav">
            <MenuItem name="add">
              <Icon type="ios-navigate"></Icon>
              新增
            </MenuItem>
            <MenuItem name="modify">
              <Icon type="ios-keypad"></Icon>
              修改
            </MenuItem>
            <MenuItem name="delete">
              <Icon type="ios-analytics"></Icon>
              删除
            </MenuItem>
          </div>
        </Menu>
      </Header>
      <Layout style="height: 100vh">
        <Sider hide-trigger :style="{background: '#fff'}">
          <div>
<!--            是不是加个回车会好点-->
            <Input v-model="searchVal" search @on-search="doSearch" @on-clear="doClearSearch" placeholder="Enter something..." clearable suffix="ios-search" style="margin-top: 5%;width: 90%" />
          </div>
          <div>
            <ul v-if="searching">
              <li></li>
            </ul>
            <Tree v-else ref="zbTree" @on-select-change="treeSelect" :data="budgetZbTreeDatas" :load-data="loadChildData" style="size: 100px;margin-left: 5%;float:left"/>
          </div>
        </Sider>
        <Layout :style="{padding: '0 10px 10px'}">
          <Content :style="{margin: '10px 0', minHeight: '280px', background: '#fff'}">
            <Table border :columns="columnDatas" :data="tableDatas"/>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  </div>
</template>

<script>

import BudgetZbEdit from '@/components/BudgetZbEdit.vue'

export default {
  name: 'BudgetZb',
  components: {
    BudgetZbEdit
  },
  data () {
    return {
      searching: false,
      pageState: 'Browse',
      showAddModal: false,
      showEditModal: false,
      modalTitle: '',
      // 当前左侧树选中的节点
      currentSelecZb: {},
      // 右侧table中展示的数据
      // tableDatas: [],
      // 右侧表格列属性
      columnDatas: [
        {
          title: '标识',
          key: 'name',
          width: 150
        },
        {
          title: '标题',
          key: 'title',
          width: 200
        },
        {
          title: '数据类型',
          key: 'dataType',
          width: 150
        },
        {
          title: '数据长度',
          key: 'dataSize',
          width: 100
        },
        {
          title: '数据精度',
          key: 'dataScale',
          width: 100
        },
        {
          title: '汇总类型',
          key: 'sumType',
          width: 150
        },
        {
          title: '说明',
          key: 'remark',
          width: 'cal(100%-850)'
        }
      ],
      // 搜索框关键字
      searchVal: '',
      // 指标树数据
      budgetZbTreeDatas: [
        {
          id: '000',
          name: 'budgetZbTreeDatas',
          parentid: '',
          title: '预算指标',
          expand: true,
          selected: false,
          children: [
            {
              id: '001',
              name: 'jine',
              parentid: '',
              title: '金额',
              dataType: '数值',
              dataSize: 8,
              dataScale: 2,
              sumType: '累加汇总',
              remark: '这是一个金额字段',
              children: [
                {
                  id: '00101',
                  name: 'jine01',
                  title: '金额01',
                  dataType: '数值',
                  dataSize: 9,
                  dataScale: 1,
                  sumType: '平均值',
                  remark: '这是另一个金额字段',
                  loading: false
                }
              ],
              loading: false
            },
            {
              id: '002',
              name: 'string',
              parentid: '',
              title: '字符',
              dataType: '字符类型',
              dataSize: 20,
              remark: '这是一个字符字段',
              children: [],
              loading: false
            }
          ],
          loading: false
        }
      ]
    }
  },
  props: {},
  watch: {
    searchVal (newVal) {
      if (newVal === '') {
        this.searching = false
      }
    },
    pageState (newVal, oldVal) {
      if (newVal === 'Add') {
        this.showAddModal = true
      } else if (newVal === 'Modify') {
        this.showEditModal = true
      } else {
        this.showAddModal = false
        this.showEditModal = false
      }
    },
    showAddModal (newVal, oldVal) {
      if (newVal === false && this.pageState !== 'Browse') {
        this.pageState = 'Browse'
      }
    },
    showEditModal (newVal, oldVal) {
      if (newVal === false && this.pageState !== 'Browse') {
        this.pageState = 'Browse'
      }
    }
  },
  computed: {
    tableDatas: function () {
      let arr = []
      let current = this.currentSelecZb
      if (current === {} || current === undefined) {
        return arr
      }
      if (current.id !== '000') {
        arr.push(current)
      }
      this.recursion(arr, current.children)
      return arr
    }
  },
  created () {
  },
  mounted () {
    this.budgetZbTreeDatas[0].selected = true
    this.currentSelecZb = this.budgetZbTreeDatas[0]
  },
  destroyed () {
  },
  methods: {
    doSearch () {
      this.searching = true
    },
    doClearSearch () {
      this.searching = false
    },
    closeModal () {
      this.pageState = 'Browse'
    },
    addBudgetZb (item) {
      if (item.children === undefined) {
        item.children = []
      }
      this.currentSelecZb.children.push(item)
      // if (!this.currentSelecZb.expand) {
      //   this.currentSelecZb.expand = true
      // }
    },
    editBudgetZb (item) {
      this.currentSelecZb = item
    },
    recursion (resultArr, currentArr) {
      if (currentArr === [] || currentArr === undefined) {
        return resultArr
      } else {
        for (let item of currentArr) {
          resultArr.push(item)
          this.recursion(resultArr, item.children)
        }
      }
    },
    // 树形的选中方法
    treeSelect () {
      // alert(this.$refs.zbTree.getSelectedNodes())
      let selectNodes = this.$refs.zbTree.getSelectedNodes()
      if (selectNodes.length !== 0) {
        this.currentSelecZb = this.$refs.zbTree.getSelectedNodes()[0]
      } else {
        this.currentSelecZb = {}
      }
    },
    menuSelect () {
      let action = this.$refs.menu.currentActiveName
      console.log(action)
      if (action === 'add') {
        if (Object.keys(this.currentSelecZb).length === 0) {
          this.$Modal.warning({
            title: '警告',
            content: '请先选中父节点！'
          })
          return
        }
        this.modalTitle = '新增指标'
        this.pageState = 'Add'
      } else if (action === 'modify') {
        if (Object.keys(this.currentSelecZb).length === 0) {
          this.$Modal.warning({
            title: '警告',
            content: '请先选中要修改的节点！'
          })
          return
        } else if (this.currentSelecZb.id === '000') {
          this.$Modal.warning({
            title: '警告',
            content: '不能修改根节点！'
          })
          return
        }
        this.modalTitle = '修改指标'
        this.pageState = 'Modify'
      }
    },
    loadChildData (item, callback) {
      setTimeout(() => {
        const data = [
          {
            title: 'children',
            loading: false,
            children: []
          },
          {
            title: 'children',
            loading: false,
            children: []
          }
        ]
        callback(data)
      }, 800)
    }
  }
}
</script>

<style lang="scss" scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    height: 100vh;
  }
  .layout-nav{
    width: 420px;
    height: 60px;
    margin: 0 auto;
    margin-left: 5px;
  }
</style>
