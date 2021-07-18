<template>



  <div v-loading="loading">


<!--
    <div class="systemConfiguration">
      <div class="operateHead">
        <el-row>
          <el-col :span="24">
            <el-form :inline="true" class="demo-form-inline" label-width="0px">
              <el-form-item>
                <div class="searchBox" style="width:420px">
                  &lt;!&ndash;<el-input v-model="" placeholder="应用系统名称" clearable style="width: 40%;"></el-input>&ndash;&gt;
                  <el-button type="primary" icon="el-icon-search" class="searchBtn" @click="handleSearch">搜索</el-button>
                  <el-button type="primary" icon="el-icon-search" class="searchBtn" @click="handleAll">查看所有</el-button>
                </div>
              </el-form-item>
              <el-form-item style="float: right;">
                <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </div>
    </div>-->




    <div style="margin-top: 10px;display: flex;justify-content: center">
      <el-input
        placeholder="默认展示部分用户，可以通过用户名搜索用户..."
        prefix-icon="el-icon-search"
        v-model="keywords" style="width: 400px" size="small">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="small" style="margin-left: 3px" @click="searchClick">搜索
      </el-button>
      <el-button type="primary" icon="el-icon-plus" size="small" style="margin-left: 3px" @click="searchClick">新增
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
  </div>
</template>
<script>
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
      /*saveUser(state){
        debugger
        if (!(isNotNullORBlank(this.article.title, this.article.mdContent, this.article.cid))) {
          this.$message({type: 'error', message: '数据不能为空!'});
          return;
        }
        var _this = this;
        _this.loading = true;
        postRequest("/article/", {
          id: _this.article.id,
          title: _this.article.title,
          mdContent: _this.article.mdContent,
          htmlContent: _this.$refs.md.d_render,
          cid: _this.article.cid,
          state: state,
          dynamicTags: _this.article.dynamicTags
        }).then(resp=> {
          _this.loading = false;
          if (resp.status == 200 && resp.data.status == 'success') {
            _this.article.id = resp.data.msg;
            _this.$message({type: 'success', message: state == 0 ? '保存成功!' : '发布成功!'});
            window.bus.$emit('blogTableReload')
            if (state == 1) {
              _this.$router.replace({path: '/articleList'});
            }
          }
        }, resp=> {
          _this.loading = false;
          _this.$message({type: 'error', message: state == 0 ? '保存草稿失败!' : '博客发布失败!'});
        })
      },*/
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
        getRequest("/admin/user?nickname=" + this.keywords).then(resp => {
          _this.loading = false;
          if (resp.status == 200) {
            _this.users = resp.data;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp => {
          _this.loading = false;
          if (resp.response.status == 403) {
            var data = resp.response.data;
            _this.$message({type: 'error', message: data});
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
        cpRoles: []
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

