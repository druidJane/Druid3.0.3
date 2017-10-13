var gulp = require('gulp');
var livereload = require('gulp-livereload'); //自动刷新页面
var del = require('del'); //清除文件
var rename = require('gulp-rename');
var cssWrap = require('gulp-css-wrap');
var mainBowerFiles = require('main-bower-files');
var bowerNormalizer = require('gulp-bower-normalize');
var uglify = require('gulp-uglify'); 
var ngAnnotate = require('gulp-ng-annotate');
var minifycss = require('gulp-minify-css');
var gulpif = require('gulp-if');
var htmlmin = require('gulp-htmlmin');

global.compress = false//是否压缩  true 、false

gulp.task('bower', function () {
    gulp.src(mainBowerFiles(), { base: './bower_components' })
        .pipe(bowerNormalizer({ bowerJson: './bower.json' }))
        .pipe(gulp.dest('dist/vendor'));
});

gulp.task('styles_css', function () {
    return gulp.src(['src/styles/**/*.css'])
		.pipe(gulpif(global.compress,minifycss()))
        .pipe(gulp.dest('dist/styles'));
});

gulp.task('styles_img', function () {
    return gulp.src(['src/styles/**/*.{png,jpg,gif,ico}'])
        .pipe(gulp.dest('dist/styles'));
});

gulp.task('scripts', function () {
    return gulp.src(['src/scripts/**/*'])
		.pipe(gulpif(global.compress,ngAnnotate({single_quotes: true})))
		.pipe(gulpif(global.compress,uglify()))
        .pipe(gulp.dest('dist/scripts'));
});

gulp.task('views_html', function () {
    return gulp.src(['./src/views/**/*.html'])
		.pipe(gulpif(global.compress,htmlmin({collapseWhitespace: true,removeComments: true})))
        .pipe(gulp.dest('dist/views'));
});

gulp.task('views_img', function () {
    return gulp.src(['./src/views/**/*.{png,jpg,gif,ico}'])
        .pipe(gulp.dest('dist/views'));
});


gulp.task('clean', function (cb) {
    del(['dist/*'], cb)
});

gulp.task('default', ['bower', 'styles_css','scripts','styles_img', 'views_html','views_img']);

// Watch
gulp.task('watch', function () {
    // Watch .scss files
    gulp.watch('src/styles/**/*', ['styles_css','styles_img']);
    gulp.watch('src/views/**/*', ['views_html','views_img']);
	gulp.watch('src/scripts/**/*', ['scripts']);
    // Create LiveReload server
    //livereload.listen();
    // Watch any files in dist/, reload on change
    //gulp.watch(['dist/**']).on('change', livereload.changed);
});
