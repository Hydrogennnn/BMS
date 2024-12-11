<template>
  <div class="home" style ="padding: 10px">

    <!-- 搜索-->
    <div style="margin: 10px 0;">
      <el-form inline="true" size="small">
        <el-form-item label="搜索bookId" >
          <el-input v-model="search1" placeholder="请输入相关内容"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item label="搜索userId" >
          <el-input v-model="search2" placeholder="请输入相关内容"  clearable>
            <template #prefix><el-icon class="el-input__icon"><search/></el-icon></template>
          </el-input>
        </el-form-item >
        <el-form-item>
          <el-button type="primary" style="margin-left: 1%" @click="load" size="mini">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="mini"  type="danger" @click="clear">重置</el-button>
        </el-form-item>
      </el-form>

    </div>
    <!-- 按钮-->
    <div style="margin: 10px 0;" >
      <el-button type="primary" @click = "add" v-if="user.role == 1">新增评论</el-button>
      <el-popconfirm title="确认删除?" @confirm="deleteBatch" v-if="user.role == 1">
        <template #reference>
          <el-button type="danger" size="mini" >批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!-- 数据字段-->
    <el-table :data="tableData" stripe border="true" @selection-change="handleSelectionChange">
      <el-table-column v-if="user.role ==1"
                       type="selection"
                       width="55">
      </el-table-column>
      <el-table-column prop="time" label="发布时间" sortable />
      <el-table-column prop="content" class-name="text-ellipsis" label="内容" />
      <el-table-column prop="readerId" label="发布者" sortable/>
      <el-table-column prop="bookId" label="书籍Id" sortable/>
      <el-table-column fixed="right" label="操作" >
        <template v-slot="scope">
          <el-button  size="mini" @click ="handleEdit(scope.row)" v-if="user.role == 1">修改</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(scope.row.id)" v-if="user.role == 1">
            <template #reference>
              <el-button type="danger" size="mini" >删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>

      <el-dialog v-model="dialogVisible" title="新增评论" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="发布时间" v-model="form.time">
            <div>
              <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" type="datetime" style="width: 80%" clearable v-model="form.time" :disabled="true"></el-date-picker>
            </div>
          </el-form-item>
          <el-form-item label="内容">
            <el-input type="textarea" style="width: 80%" v-model="form.content" :autosize="{ minRows: 1, maxRows: 10 }"></el-input>
          </el-form-item>
          <el-form-item label="发布者ID">
            <el-input style="width: 80%" v-model="form.readerId"></el-input>
          </el-form-item>
          <el-form-item label="书籍ID">
            <el-input style="width: 80%" v-model="form.bookId"></el-input>
          </el-form-item>

        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
        </template>
      </el-dialog>

      <el-dialog v-model="dialogVisible2" title="修改评论信息" width="30%">
        <el-form :model="form" label-width="120px">

          <el-form-item label="发布时间" v-model="form.time">
            <div>
              <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" type="datetime" style="width: 80%" clearable v-model="form.time" :disabled="true"></el-date-picker>
            </div>
          </el-form-item>
          <el-form-item label="内容">
            <el-input type="textarea" style="width: 80%" v-model="form.content" :autosize="{ minRows: 1, maxRows: 10 }"></el-input>
          </el-form-item>
          <el-form-item label="发布者ID">
            <el-input style="width: 80%" v-model="form.readerId"></el-input>
          </el-form-item>
          <el-form-item label="书籍ID">
            <el-input style="width: 80%" v-model="form.bookId"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "../utils/request";
import {ElMessage} from "element-plus";
import moment from "moment";
export default {
  created(){
    let userStr = sessionStorage.getItem("user") ||"{}"
    this.user = JSON.parse(userStr)
    this.load()
  },
  name: 'Comment',
  methods: {
    // (this.isbnArray.indexOf(scope.row.isbn)) == -1
    handleSelectionChange(val){
      this.ids = val.map(v =>v.id)
    },
    deleteBatch(){
      if (!this.ids.length) {
        ElMessage.warning("请选择数据！")
        return
      }
      //  一个小优化，直接发送这个数组，而不是一个一个的提交删除
      request.post("/comment/deleteBatch",this.ids).then(res =>{
        if(res.code === '0'){
          ElMessage.success("批量删除成功")
          this.load()
        }
        else {
          ElMessage.error(res.msg)
        }
      })
    },
    load(){
      this.numOfOutDataBook =0;
      this.outDateBook =[];
      request.get("/comment",{
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          book_id: this.search1,
          user_id: this.search2,
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    clear(){
      this.search1 = ""
      this.search2 = ""
      this.search3 = ""
      this.load()
    },

    handleDelete(id){
      request.delete("/comment/" + id ).then(res =>{
        console.log(res)
        if(res.code == 0 ){
          ElMessage.success("删除成功")
        }
        else
          ElMessage.error(res.msg)
        this.load()
      })
    },
    add(){
      this.dialogVisible= true
      this.form ={}
      this.form.time = this.CurrentDate();
      this.form.userId = this.user.id;
    },
    async save(){
      //ES6语法
      //地址,但是？IP与端口？+请求参数
      // this.form?这是自动保存在form中的，虽然显示时没有使用，但是这个对象中是有它的
      const res = await request.get("/book/"+ this.form.bookId);
      if(res.data.length==0) {
        ElMessage("书籍不存在");
        return;
      }
      const res2 = await request.get("/user/usersearch", {
        params: {
          search1: this.form.userId
        }
      })
      if(res2.data.records.length == 0){
        ElMessage("用户不存在");
        return;
      }

      if(this.form.id){
        request.put("/comment",this.form).then(res =>{
          if(res.code == 0){
            ElMessage({
              message: '修改新闻成功',
              type: 'success',
            })
          }
          else {
            ElMessage.error(res.msg)
          }

          this.load()
          this.dialogVisible2 = false
        })
      }
      else {
        request.post("/comment",this.form).then(res =>{
          console.log(res)
          if(res.code == 0){
            ElMessage.success('发布新闻成功')
          }
          else {
            ElMessage.error(res.msg)
          }
          this.load()
          this.dialogVisible = false
        })
      }

    },
    // formatter(row) {:formatter="formatter"
    //   return row.address
    // },

    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible2 = true;
      this.form.userId = this.user.id;
      this.form.time = this.CurrentDate();
      // console.log(this.form.time)
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    toLook(){
      this.dialogVisible3 =true;
    },
    CurrentDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
  },
  data() {
    return {
      form: {},
      form2:{},
      form3:{},
      dialogVisible: false,
      dialogVisible2: false,
      search1:'',
      search2:'',
      total:10,
      currentPage:1,
      pageSize: 10,
      tableData: [],
      user:{},
      number:0,
      bookData:[],
      isbnArray:[],
      outDateBook:[],
      numOfOutDataBook: 0
    }
  },
}
</script>
<style>
.text-ellipsis .cell{
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>