CMD="/usr/lib/jvm/java-13-openjdk/bin/java --add-modules javafx.base,javafx.graphics --add-reads javafx.base=ALL-UNNAMED --add-reads javafx.graphics=ALL-UNNAMED --enable-preview -javaagent:/opt/intellij-idea-ultimate-edition/lib/idea_rt.jar=33687:/opt/intellij-idea-ultimate-edition/bin -Dfile.encoding=UTF-8 -classpath /home/dylan/workspace/CS211/othello-java/bin/production/othello-java:/home/dylan/workspace/CS211/othello-java/lib/javafx-swt.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.base.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.controls.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.fxml.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.graphics.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.media.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.swing.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.web.jar -p /home/dylan/workspace/CS211/othello-java/lib/javafx.base.jar:/home/dylan/workspace/CS211/othello-java/lib/javafx.graphics.jar "



for m in $(<modules); do
    CMD="$CMD --add-exports $m=ALL-UNNAMED"
done

cd bin/production/othello-java

CMD="$CMD gui.Main"
echo $CMD

$CMD
#OUTPUT="$($CMD 2>&1)"
echo $OUTPUT
MODULE="$(echo "$OUTPUT" | grep -oP '(?<=because module ).*(?= does not export)')"
PACKAGE="$(echo "$OUTPUT" | grep -oP '(?<=does not export ).*(?= to unnamed)')"
#echo $MODULE/$PACKAGE
#echo "$MODULE/$PACKAGE" >> modules
