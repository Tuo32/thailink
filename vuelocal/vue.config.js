const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

const path = require("path");

module.exports = {

  pluginOptions: {

    'style-resources-loader': {

      preProcessor: 'sass',

      patterns: []

    }

  }

}
