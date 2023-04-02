const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
require('dotenv').config();

let target = 'web';
let mode = 'development';
let plugins = [
  new MiniCssExtractPlugin(),
  new HtmlWebpackPlugin({
    template: './public/index.html',
    inject: 'body',
  }),
];

if (process.env.NODE_ENV === 'production') {
  mode = 'production';
  target = 'browserslist';
} else {
  plugins.push(
    new ESLintPlugin({
      extensions: ['js', 'jsx'],
    })
  );
}

if (process.env.SERVE) {
  plugins.push(new ReactRefreshWebpackPlugin());
}

let isDev = mode === 'development';

module.exports = {
  mode: mode,
  target: target,
  entry: './src/index.js',
  output: {
    path: path.resolve(__dirname, 'dist'),
    assetModuleFilename: 'images/[hash][ext][query]',
    clean: true,
    filename: '[name].[contenthash].js',
  },
  module: {
    rules: [
      {
        test: /\.(s[ac]|c)ss$/,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            options: {
              publicPath: '',
            },
          },
          'css-loader',
          'postcss-loader',
          'sass-loader',
        ],
      },
      {
        test: /\.jsx?$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
        },
      },
      {
        test: /\.(png|jpe?g|gif|svg)$/i,
        type: 'asset/resource',
      },
      {
        test: /\.(ttf|woff2?|eon|otf)$/,
        type: 'asset/resource',
      },
    ],
  },
  plugins: plugins,
  resolve: {
    extensions: ['.js', '.jsx'],
  },
  devtool: isDev ? 'source-map' : false,
  devServer: {
    static: {
      directory: path.join(__dirname, 'public'),
    },
    proxy: {
      '/api': {
        target: process.env.proxyHost || 'http://localhost/',
        secure: false,
        changeOrigin: true,
      }
    },
    historyApiFallback: true,
    open: true,
    hot: true,
  },
};