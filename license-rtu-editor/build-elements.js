
const fs = require('fs-extra');
const concat = require('concat');

(async function build() {
  const files = [
    './dist/license-rtu-editor/runtime.js',
    './dist/license-rtu-editor/polyfills.js',
    './dist/license-rtu-editor/scripts.js',
    './dist/license-rtu-editor/main.js'
  ];

  await fs.ensureDir('elements');
  await concat(files, 'elements/license-rtu-editor.js');
})();