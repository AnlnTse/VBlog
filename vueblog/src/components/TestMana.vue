<template>
  <div v-loading="loading">


    <div style="margin-top: 10px;display: flex;justify-content: center">
      <el-input
        placeholder="默认展示部分用户，可以通过用户名搜索用户..."
        prefix-icon="el-icon-search"
        v-model="keywords" style="width: 400px" size="small">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="small" style="margin-left: 3px" @click="searchClick">搜索
      </el-button>
      <el-button type="primary" icon="el-icon-plus" size="small" style="margin-left: 3px" @click="handleAdd">新增
      </el-button>
    </div>


    <div style="display: flex;justify-content: space-around;flex-wrap: wrap">
      <el-card style="width:330px;margin-top: 10px;" v-for="(user,index) in users" :key="index"
               v-loading="cardloading[index]">
        <div slot="header" style="text-align: left">
          <span>{{user.nickname}}</span>
          <el-button style="float: right; padding: 3px 0;color: #ff0509" type="text" icon="el-icon-delete"
                     @click="deleteUser(user.id)">删除
          </el-button>
        </div>
        <div>
          <!-- <div><img :src="user.userface" :alt="user.nickname" style="width: 70px;height: 70px"></div>-->
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>用户名:</span><span>{{user.username}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>电子邮箱:</span><span>{{user.email}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>注册时间:</span><span>{{user.regTime | formatDateTime}}</span>
          </div>
          <div
            style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px;display: flex;align-items: center">
            <span>用户状态:</span>
            <el-switch
              v-model="user.enabled"
              active-text="启用"
              active-color="#13ce66"
              @change="enabledChange(user.enabled,user.id,index)"
              inactive-text="禁用" style="font-size: 12px">
            </el-switch>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>用户角色:</span>
            <el-tag
              v-for="role in user.roles"
              :key="role.id"
              size="mini"
              style="margin-right: 8px"
              type="success">
              {{role.name}}
            </el-tag>
            <el-popover
              placement="right"
              title="角色列表"
              width="200"
              :key="index+''+user.id"
              @hide="saveRoles(user.id,index)"
              trigger="click" v-loading="eploading[index]">
              <el-select v-model="roles" :key="user.id" multiple placeholder="请选择" size="mini">
                <el-option
                  v-for="(item,index) in allRoles"
                  :key="user.id+'-'+item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
              <el-button type="text" icon="el-icon-more" style="padding-top: 0px" slot="reference"
                         @click="showRole(user.roles,user.id,index)"></el-button>
            </el-popover>
          </div>
        </div>
      </el-card>
    </div>


    <el-dialog :close-on-click-modal="false"
               :title="dialogTitle"
               :visible.sync="dialogVisible"
               @close="handleClose"
               width="500px">
      <el-form :model="formInline" :rules="rules" ref="ruleForm" label-width="130px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formInline.username" v-if="dialogType == 'add'"></el-input>
          <el-input v-model="formInline.username" v-else disabled></el-input>
        </el-form-item>
        <!--<el-form-item label="应用系统名称" prop="username">
          <el-input v-model="formInline.username" @blur="handleName" :disabled="dialogType == 'update'"></el-input>
        </el-form-item>-->
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="formInline.email""></el-input>
        </el-form-item>


        <el-form-item label="密码：" prop="password">
          <el-input placeholder="请输入密码" v-model="formInline.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码：" prop="confirmPassword">
          <el-input placeholder="请输入确认密码" v-model="formInline.confirmPassword" show-password></el-input>
        </el-form-item>

        <!--<el-form-item label="部门" prop="departmentId">
          <el-select v-model="formInline.departmentId" style="width: 330px;" ref="selectDept"
                     :disabled="dialogType == 'update'">
            <el-option :value="formInline.departmentId" :label="formInline.departmentName"
                       style="width: 330px; height: 180px; overflow: auto;">
              <el-tree :data="departmentTree"
                       :props="defaultProps"
                       @node-click="handleNodeClick">
              </el-tree>
            </el-option>
          </el-select>
        </el-form-item>-->
        <!--<el-form-item label="联系人" prop="user">
          <el-select v-model="formInline.user" filterable style="width: 330px;" @change="changeOwner">
            <el-option v-for="item in userData" :key="item.id" :label="item.email" :value="item.id"></el-option>
          </el-select>
        </el-form-item>-->
        <!--<el-form-item label="联系人电话" prop="mobile">
          <el-input v-model="formInline.mobile" :disabled="dialogType == 'update'"></el-input>
        </el-form-item>
        <el-form-item label="部署目录" prop="deployFolder">
          <el-input v-model="formInline.deployFolder" :disabled="dialogType == 'update'"></el-input>
        </el-form-item>
        <el-form-item label="系统说明" prop="description">
          <el-input v-model="formInline.description" type="textarea" :disabled="dialogType == 'update'"></el-input>
        </el-form-item>-->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </span>
    </el-dialog>


  </div>
</template>
<script>
  import {postRequest} from '../utils/api'
  import {getRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'

  export default {
    mounted: function () {
      this.loading = true;
      this.loadUserList();
      this.cardloading = Array.apply(null, Array(20)).map(function (item, i) {
        return false;
      });
      this.eploading = Array.apply(null, Array(20)).map(function (item, i) {
        return false;
      });
    },
    methods: {

      // 弹框关闭重置校验
      handleClose() {
        this.$refs['ruleForm'].resetFields()
      },

      // 新增
      handleAdd() {
        this.dialogTitle = '新增用户信息'
        this.formInline = {
          id: '',
          username: '',
          appSystemName: '',
          email: '',
          password: '',
          departmentId: '',
          departmentName: '',
          user: '',
          mobile: '',
          deployFolder: '',
          description: ''
        }
        this.dialogVisible = true
        this.dialogType = "add"
      },

      // 新增/修改保存
      handleSave() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            let url = ''
            let params = {}
            let message = ''
            if (this.dialogTitle == '新增用户信息') {
              debugger
              url = '/admin/user/add'
              params = {
                username: this.formInline.username,
                email: this.formInline.email,
                password: this.formInline.password,
              }
            }
            console.log(params)
            var _this = this;
            postRequest(url, params).then(resp => {
              if (resp.status == 200) {
                var json = resp.data;
                _this.$message({type: json.status, message: json.msg});
                debugger
                if (json.status == 'success') {
                  _this.dialogVisible = false
                }
                _this.loadUserList();
                return
              }
            }, resp => {
              debugger
              if (resp.response.status == 403) {
                _this.$message({
                  type: 'error',
                  message: resp.response.data
                });
              }
              _this.loading = false;
            });
          }
        });
      },

      saveRoles(id, index) {
        var selRoles = this.roles;
        if (this.cpRoles.length == selRoles.length) {
          for (var i = 0; i < this.cpRoles.length; i++) {
            for (var j = 0; j < selRoles.length; j++) {
              if (this.cpRoles[i].id == selRoles[j]) {
                selRoles.splice(j, 1);
                break;
              }
            }
          }
          if (selRoles.length == 0) {
            return;
          }
        }
        var _this = this;
        _this.cardloading.splice(index, 1, true)
        putRequest("/admin/user/role", {rids: this.roles, id: id}).then(resp => {
          if (resp.status == 200 && resp.data.status == 'success') {
            _this.$message({type: resp.data.status, message: resp.data.msg});
            _this.loadOneUserById(id, index);
          } else {
            _this.cardloading.splice(index, 1, false)
            _this.$message({type: 'error', message: '更新失败!'});
          }
        }, resp => {
          _this.cardloading.splice(index, 1, false)
          if (resp.response.status == 403) {
            var data = resp.response.data;
            _this.$message({type: 'error', message: data});
          }
        });
      },
      showRole(aRoles, id, index) {
        this.cpRoles = aRoles;
        this.roles = [];
        this.loadRoles(index);
        for (var i = 0; i < aRoles.length; i++) {
          this.roles.push(aRoles[i].id);
        }
      },
      handleAddUser() {
        var _this = this;
        this.$prompt('请输入用户名称', '新增用户', {
          confirmButtonText: '更新',
          inputValue: row.cateName,
          cancelButtonText: '取消'
        }).then(({value}) => {
          //value就是输入值
          if (value == null || value.length == 0) {
            _this.$message({
              type: 'info',
              message: '数据不能为空!'
            });
          } else {
            _this.loading = true;
            putRequest("/admin/category/", {id: row.id, cateName: value}).then(resp => {
              var json = resp.data;
              _this.$message({
                type: json.status,
                message: json.msg
              });
              _this.refresh();
            }, resp => {
              if (resp.response.status == 403) {
                _this.$message({
                  type: 'error',
                  message: resp.response.data
                });
              }
              _this.loading = false;
            });
          }
        });
      },
      deleteUser(id) {
        var _this = this;
        this.$confirm('删除该用户, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.loading = true;
          debugger
          deleteRequest("/admin/user/" + id).then(resp => {
            if (resp.status == 200 && resp.data.status == 'success') {
              _this.$message({type: 'success', message: '删除成功!'})
              _this.loadUserList();
              return;
            }
            _this.loading = false;
            _this.$message({type: 'error', message: '删除失败!'})
          }, resp => {
            _this.loading = false;
            _this.$message({type: 'error', message: '删除失败!'})
          });
        }).catch(() => {
          debugger
          _this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      enabledChange(enabled, id, index) {
        var _this = this;
        _this.cardloading.splice(index, 1, true)
        putRequest("/admin/user/enabled", {enabled: enabled, uid: id}).then(resp => {
          if (resp.status != 200) {
            _this.$message({type: 'error', message: '更新失败!'})
            _this.loadOneUserById(id, index);
            return;
          }
          _this.cardloading.splice(index, 1, false)
          _this.$message({type: 'success', message: '更新成功!'})
        }, resp => {
          _this.$message({type: 'error', message: '更新失败!'})
          _this.loadOneUserById(id, index);
        });
      },
      loadRoles(index) {
        var _this = this;
        _this.eploading.splice(index, 1, true)
        getRequest("/admin/roles").then(resp => {
          _this.eploading.splice(index, 1, false)
          if (resp.status == 200) {
            _this.allRoles = resp.data;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp => {
          _this.eploading.splice(index, 1, false)
          if (resp.response.status == 403) {
            var data = resp.response.data;
            _this.$message({type: 'error', message: data});
          }
        });
      },
      loadOneUserById(id, index) {
        var _this = this;
        getRequest("/admin/user/" + id).then(resp => {
          _this.cardloading.splice(index, 1, false)
          if (resp.status == 200) {
            _this.users.splice(index, 1, resp.data);
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp => {
          _this.cardloading.splice(index, 1, false)
          if (resp.response.status == 403) {
            var data = resp.response.data;
            _this.$message({type: 'error', message: data});
          }
        });
      },
      loadUserList() {
        var _this = this;
        // getRequest("/admin/userByNickName?nickname=" + this.keywords).then(resp => {
        getRequest("/admin/users").then(resp => {
          _this.loading = false;
          if (resp.status == 200) {
            _this.users = resp.data;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        });
      },
      searchClick() {
        this.loading = true;
        this.loadUserList();
      }
    },

    data() {
      return {
        loading: false,
        eploading: [],
        cardloading: [],
        keywords: '',
        users: [],
        allRoles: [],
        roles: [],
        cpRoles: [],


        userData: [], // 所有用户
        dialogVisible: false, // 新增/修改弹框展示
        dialogTitle: '新增用户',// 新增/修改弹框标题
        dialogType: "", // 类型是新增，还是修改
        formInline: { // 新增/修改弹框表单
          username: '',
          appSystemName: '',
          email: '',
          password: '',
          confirmPassword: '',
          departmentId: '',
          departmentName: '',
          user: '',
          mobile: '',
          deployFolder: '',
          description: ''
        },

        changePassword: {
          password: '',
          confirmPassword: ''
        },


        rules: { // 新增/修改弹框校验
          username: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          appSystemName: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          departmentId: [
            {required: true, message: '请选择', trigger: 'blur'}
          ],
          user: [
            {required: true, message: '请选择', trigger: 'blur'}
          ],
          mobile: [
            {pattern: /^1[3|4|5|7|8|9][0-9]\d{8}$/, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          deployFolder: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
          description: [
            {required: true, message: '请输入', trigger: 'blur'}
          ],
        },


      }
    }
  }
</script>


<style>
  .systemConfiguration .operateHead .el-tabs__nav-wrap::after {
    height: 0;
  }

  .systemConfiguration .el-tabs__item,
  .systemConfiguration .el-tabs__item:hover,
  .systemConfiguration .el-tabs__item.is-active {
    color: #0c82b4;
  }

  .systemConfiguration .el-tabs__active-bar {
    background-color: #0c82b4;
  }

  .mallDeploy .el-input__inner {
    width: 50%;
  }
</style>
<style scoped>
  .operateHead {
    padding: 20px 20px 0;
    background-color: #FFFFFF;
  }

  .refresh {
    margin-top: 20px;
  }

  .mallDeploy {
    background-color: #FFFFFF;
    padding: 20px 30px 30px;
    margin-top: 20px;
  }

  .title_box {
    font-size: 16px;
    font-weight: 600;
    color: #446083;
  }
</style>

