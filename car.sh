mkdir -v build build/res
cp -v *.java build/ &&
cp -vr res/* build/res/ &&
cp -v Manifest.txt build/ &&
cd build &&
javac -verbose *.java &&
jar cvfm Game.jar Manifest.txt * &&
mv -v Game.jar ../Game.jar &&
cd .. &&
rm -rv build &&
java -jar Game.jar
