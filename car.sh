echo "Working..." &&
mkdir -v build build/res &&
cp -v *.java build/ &&
cp -vr res/* build/res/ &&
cp -v Manifest.txt build/ &&
cd build &&
echo "Compiling..." &&
javac *.java &&
echo "Compressing..." &&
jar cvfm Game.jar Manifest.txt * &&
mv -v Game.jar ../Game.jar &&
cd .. &&
echo "Cleaning..." &&
rm -rv build &&
echo "Done."