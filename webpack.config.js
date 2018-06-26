var webpack = require('webpack');

module.exports = {
    entry: './ui/entry.js',
    output: {path: __dirname + '/public/compiled', filename: 'bundle.js'},
    module: {
        loaders: [
            {
                test: /\.jsx?$/, loader: 'babel-loader',
                include: /ui/, query: {presets: ['env', 'stage-0', 'react']}
            }

        ]
    }
};