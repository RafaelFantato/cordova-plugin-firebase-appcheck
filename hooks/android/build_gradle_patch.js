#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

module.exports = function (context) {
  const rootGradle = path.join(context.opts.projectRoot, 'platforms/android/build.gradle');
  const appGradle = path.join(context.opts.projectRoot, 'platforms/android/app/build.gradle');

  // Adiciona o classpath no build.gradle raiz
  let buildGradleContent = fs.readFileSync(rootGradle, 'utf8');
  if (!buildGradleContent.includes('com.google.gms:google-services')) {
    buildGradleContent = buildGradleContent.replace(/dependencies\s*{/, match =>
      `${match}\n        classpath 'com.google.gms:google-services:4.4.1'`
    );
    fs.writeFileSync(rootGradle, buildGradleContent, 'utf8');
    console.log('✅ [AppCheck] classpath do google-services adicionado ao build.gradle');
  }

  // Aplica o plugin no build.gradle do app
  let appGradleContent = fs.readFileSync(appGradle, 'utf8');
  if (!appGradleContent.includes("apply plugin: 'com.google.gms.google-services'")) {
    appGradleContent += `\napply plugin: 'com.google.gms.google-services'\n`;
    fs.writeFileSync(appGradle, appGradleContent, 'utf8');
    console.log('✅ [AppCheck] apply plugin do google-services adicionado ao app/build.gradle');
  }
};