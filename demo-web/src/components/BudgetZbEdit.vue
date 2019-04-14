<comment>
    # 组件注释
</comment>
<template>
  <Form ref="budgetZb" :model="budgetZb" :rules="ruleValidate" :label-width="100">
    <FormItem label="标识：" prop="name">
      <Input v-model="budgetZb.name" placeholder="指标标识" :disabled="doEdit"/>
    </FormItem>
    <FormItem label="标题：" prop="title">
      <Input v-model="budgetZb.title" placeholder="指标标题"/>
    </FormItem>
    <FormItem label="数据类型：" prop="dataType">
      <Select v-model="budgetZb.dataType" placeholder="请选择数据类型" @on-change="dataTypeSelect" :disabled="doEdit">
        <Option v-for="item in dataType" :value="item" :key="item">{{item}}</Option>
      </Select>
    </FormItem>
    <FormItem label="数据长度：" prop="dataSize">
      <Input v-model="budgetZb.dataSize" placeholder="数据长度" :number="true" :disabled="dataSizeDisabled || doEdit"/>
    </FormItem>
    <FormItem label="数据精度：" prop="dataScale">
      <Input v-model="budgetZb.dataScale" placeholder="数据精度" :number="true" :disabled="dataScaleDisabled || doEdit"/>
    </FormItem>
    <FormItem label="汇总类型：" prop="sumType">
      <Select v-model="budgetZb.sumType" placeholder="请选择汇总类型" :disabled="sumTypeDisabled || doEdit">
        <Option v-for="item in sumType" :value="item" :key="item">{{item}}</Option>
      </Select>
    </FormItem>
    <FormItem label="指标备注：" prop="remark">
      <Input v-model="budgetZb.remark" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="指标说明..."/>
    </FormItem>
    <FormItem>
      <Button @click="handleReset('budgetZb')" style="float: right">取消</Button>
      <Button type="primary" @click="handleSubmit('budgetZb')" style="float: right;margin-right: 5%">确定</Button>
    </FormItem>
  </Form>
</template>
<script>
export default {
  name: 'BudgetZbEdit',
  props: {
    budgetZb: {
      required: false,
      default: () => {
        return {}
      }
    },
    doEdit: {
      dataType: Boolean,
      required: true
    }
  },
  data () {
    // 数据长度验证，仅数值型、整数型、百分比型、字符型会校验长度
    const validateDataSize = (rule, value, callback) => {
      if (this.budgetZb.dataType === '数值' ||
        this.budgetZb.dataType === '整数型' ||
        this.budgetZb.dataType === '百分比类型' ||
        this.budgetZb.dataType === '字符类型') {
        if (value === null || value === undefined) {
          return callback(new Error('当数据类型为' + this.budgetZb.dataType + '时，数据长度不能为空！'))
        } else {
          var r = /^\+?[1-9][0-9]*$/
          if (r.test(value)) {
            callback()
          } else {
            return callback(new Error('数据长度必须是正整数！'))
          }
        }
      }
      callback()
    }
    // 数据精度验证，仅数值型、百分比型会验证精度
    const validateDataScale = (rule, value, callback) => {
      if (this.budgetZb.dataType === '数值' ||
        this.budgetZb.dataType === '百分比类型') {
        if (value === null || value === undefined) {
          return callback()
        } else {
          var r = /^\+?[1-9][0-9]*$/
          if (r.test(value)) {
            if (value > this.budgetZb.dataSize) {
              return callback(new Error('数据精度不能大于数据长度！'))
            } else {
              return callback()
            }
          } else {
            return callback(new Error('数据精度必须是正整数！'))
          }
        }
      }
      callback()
    }
    return {
      // 汇总类型是否可用
      sumTypeDisabled: true,
      // 数据精度是否可用
      dataScaleDisabled: true,
      // 数据长度是否可用
      dataSizeDisabled: true,
      dataType: [
        '货币', '数值', '整数型', '百分比类型', '附件类型', '枚举类型', '日期类型', '字符类型', '备注类型'
      ],
      sumType: [
        '不汇总', '累加汇总', '平均值', '最大值', '最小值'
      ],
      // budgetZb: {
      //   id: '',
      //   name: '',
      //   title: '',
      //   dataType: '',
      //   sumType: '',
      //   remark: ''
      // },
      ruleValidate: {
        name: [
          { required: true, message: '指标标识不能为空！', trigger: 'blur' },
          { required: true, type: 'string', max: 20, message: '指标标识长度不能大于20', trigger: 'change' }
        ],
        title: [
          { required: true, message: '指标标题不能为空！', trigger: 'blur' },
          { required: true, type: 'string', max: 20, message: '指标名称长度不能大于200', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '数据类型必选！', trigger: 'blur' }
        ],
        dataSize: [
          { validator: validateDataSize, trigger: 'blur' }
        ],
        dataScale: [
          { validator: validateDataScale, trigger: 'blur' }
        ],
        desc: [
          { type: 'string', max: 500, message: '备注最多只能写500个字！', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    dataTypeSelect () {
      if (this.budgetZb.dataType === '货币' ||
        this.budgetZb.dataType === '附件类型' ||
        this.budgetZb.dataType === '枚举类型' ||
        this.budgetZb.dataType === '日期类型' ||
        this.budgetZb.dataType === '备注类型') {
        this.budgetZb.dataSize = null
        this.budgetZb.dataScale = null
        this.budgetZb.sumType = null
        this.dataScaleDisabled = true
        this.dataSizeDisabled = true
        this.sumTypeDisabled = true
      } else if (this.budgetZb.dataType === '数值' ||
        this.budgetZb.dataType === '百分比类型') {
        this.dataScaleDisabled = false
        this.dataSizeDisabled = false
        this.sumTypeDisabled = false
      } else if (this.budgetZb.dataType === '字符类型') {
        this.budgetZb.sumType = null
        this.sumTypeDisabled = true
      } else if (this.budgetZb.dataType === '整数型') {
        this.budgetZb.dataScale = null
        this.dataScaleDisabled = true
        this.dataSizeDisabled = false
        this.sumTypeDisabled = false
      }
    },
    handleSubmit (name) {
      let doEdit = this.doEdit
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (doEdit) {
            this.$parent.$emit('editBudgetZb', this.budgetZb)
          } else {
            // 新建指标对象，防止内存引用
            var newZb = Object.assign({}, this.budgetZb)
            this.$parent.$emit('addBudgetZb', newZb)
            this.budgetZb = {}
            this.$refs[name].resetFields()
          }
          this.$Message.success('Success!')
          this.$parent.$emit('closeModal')
          // this.budgetZb = {}
        } else {
          this.$Message.error('校验失败!')
        }
      })
    },
    handleReset (name) {
      this.$parent.$emit('closeModal')
    }
  }
}
</script>
