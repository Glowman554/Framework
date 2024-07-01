set BUILD_FOLDER (pwd)/build
set BUILD_ROOT (pwd)

function build
    cd $BUILD_ROOT
    
    echo "Building: $argv[1]"
    git clone https://github.com/Glowman554/Framework/ -b $argv[1] $argv[1]

    
    cd $argv[1]
    ./gradlew remapJar

    cp build/libs/*.jar $BUILD_FOLDER
end


mkdir -p $BUILD_FOLDER

build 1.20.4
build 1.20.5
build 1.20.6
build 1.21