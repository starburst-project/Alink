FROM nginx:1.17.0-alpine

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories

# Copy the react build from Stage 1
ARG DIST_FOLDER=dist
COPY ${DIST_FOLDER}/ /var/www/

# Copy our custom nginx config
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80 to the Docker host, so we can access it
# from the outside.
EXPOSE 9090

ENTRYPOINT ["nginx","-g","daemon off;"]
