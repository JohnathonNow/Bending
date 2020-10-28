for i in *.java; do
    filename="${i%.*}"
    echo "this.register(new $filename());"
done
