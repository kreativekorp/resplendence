CLASSPATH := $(shell find dep -name \*.jar | tr '\n' : | sed s/:$$//)
SRCFILES := $(shell find src -name \*.java \! -path \*/jd1/\* \! -path \*/jd2/\* \! -path \*/ten/\*)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java \! -path \*/jd1/\* \! -path \*/jd2/\* \! -path \*/ten/\* | sed -E s/^src\\/\|\\/[^\\/]+$$//g | sort | uniq | tr / .)

all: clean bin doc Resplendence.jar Resplendence.app

eclipseall: eclipseclean Resplendence.jar Resplendence.app

clean:
	rm -rf bin
	rm -rf doc
	rm -rf *.jar
	rm -rf Resplendence.app

eclipseclean:
	rm -rf *.jar
	rm -rf Resplendence.app

bin: src
	mkdir -p bin/META-INF/services
	javac -cp "./bin:$(CLASSPATH)" -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc -cp "./bin:$(CLASSPATH)" -sourcepath src $(PACKAGES) -d doc

Resplendence.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF Resplendence.jar -C bin META-INF -C bin com/kreative -C bin glguerin
	chmod +x Resplendence.jar

Resplendence.app: dep bin Resplendence.jar
	mkdir -p Resplendence.app/Contents/MacOS
	mkdir -p Resplendence.app/Contents/Resources/Java
	cp -f dep/PkgInfo Resplendence.app/Contents
	cp -f dep/Info.plist Resplendence.app/Contents
	cp -f dep/universalJavaApplicationStub Resplendence.app/Contents/MacOS/Resplendence
	cp -f dep/*.icns Resplendence.app/Contents/Resources
	cp -f dep/*.jar Resplendence.app/Contents/Resources/Java
	cp -f bin/*.dff Resplendence.app/Contents/Resources/Java
	cp -f Resplendence.jar Resplendence.app/Contents/Resources/Java

.PHONY: all eclipseall clean eclipseclean
