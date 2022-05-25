module.exports = {
  lintOnSave: false,
  productionSourceMap: false,
  devServer: {
    //host: '0.0.0.0',
    port: '8082',
    disableHostCheck: true,
    proxy: {
      // 注意：精准匹配的选项要配置在前面
      '/api/': {
        //target: 'http://localhost:8080',
        target: 'http://localhost:8080',
        secure: false,
        ws:true, 
        changeOrigin:true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },

  //publicPath: '/static/', // 这个指向的地址就是打包后,html文件引用js文件的路径地址
 
}
