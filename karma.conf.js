module.exports = function (config) {
    config.set({
        browsers: ['ChromiumHeadless'],
        // The directory where the output file lives
        basePath: 'resources/test',
        // The file itself
        files: ['ci.js'],
        frameworks: ['cljs-test'],
        plugins: ['karma-cljs-test', 'karma-chrome-launcher'],
        colors: true,
        logLevel: config.LOG_INFO,
        client: {
            args: ["shadow.test.karma.init"],
            singleRun: true
        },
        proxies: {
            '/mockServiceWorker.js': '/mockServiceWorker.js',
        }
    })
};
