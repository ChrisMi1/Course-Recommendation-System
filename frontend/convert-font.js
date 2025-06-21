const fs = require('fs');

const fontPath = './src/assets/fonts/Roboto-Regular.ttf'; // adjust path if needed
const outputPath = './src/assets/fonts/Roboto-Regular.js';

const fontData = fs.readFileSync(fontPath);
const base64Font = fontData.toString('base64');

const jsContent = `const RobotoRegular = "${base64Font}";\nexport default RobotoRegular;\n`;

fs.writeFileSync(outputPath, jsContent);

console.log('Roboto-Regular.js has been created successfully!');
