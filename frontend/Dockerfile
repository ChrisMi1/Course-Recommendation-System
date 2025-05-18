FROM node:14-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
RUN npm install -g serve
EXPOSE 80

# Start serve to host the built files
CMD ["serve", "-s", "build", "-l", "80"]