CMD=' --add-modules javafx.base,javafx.graphics --add-reads javafx.base=ALL-UNNAMED --add-reads javafx.graphics=ALL-UNNAMED "-javaagent:/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 2019.2.1/lib/idea_rt.jar=53462:/c/Program Files/JetBrains/IntelliJ IDEA Community Edition 2019.2.1/bin" -Dfile.encoding=UTF-8 -classpath "/c/Users/minhd/OneDrive - George Mason University/Documents/IntelliJ/othello-java/target/classes;/c/Program Files/Java/jdk-13.0.1/lib/javafx-swt.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.base.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.controls.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.fxml.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.graphics.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.media.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.swing.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.web.jar" -p "/c/Program Files/Java/jdk-13.0.1/lib/javafx.base.jar;/c/Program Files/Java/jdk-13.0.1/lib/javafx.graphics.jar" '



for m in $(<modules); do
    CMD="$CMD --add-exports $m=ALL-UNNAMED"
done

cd bin/production/othello-java

CMD="$CMD gui.Main"
echo $CMD

"/c/Program Files/Java/jdk-13.0.1/bin/java" $CMD
#OUTPUT="$($CMD 2>&1)"
echo $OUTPUT
MODULE="$(echo "$OUTPUT" | grep -oP '(?<=because module ).*(?= does not export)')"
PACKAGE="$(echo "$OUTPUT" | grep -oP '(?<=does not export ).*(?= to unnamed)')"
#echo $MODULE/$PACKAGE
#echo "$MODULE/$PACKAGE" >> modules
