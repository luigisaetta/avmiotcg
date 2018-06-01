# the pipeline for entire build
./packjar.sh
cd /Users/lsaetta/Progetti/avmiotcg/iotgateway/target
# rename to iotgateway.jar
./rename.sh
cp iotgateway.jar /Users/lsaetta/Progetti/avmiotcg/iotgateway/docker

