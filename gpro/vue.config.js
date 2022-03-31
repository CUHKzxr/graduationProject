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
        target: 'http://localhost:8080',
        secure: false,
        ws:true, 
        changeOrigin:true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
 
}
