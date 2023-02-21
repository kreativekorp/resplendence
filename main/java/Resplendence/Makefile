CLASSPATH := $(shell find dep -name \*.jar | tr '\n' : | sed s/:$$//)
SRCFILES := $(shell find src -name \*.java \! -path \*/jd1/\* \! -path \*/jd2/\* \! -path \*/ten/\*)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java \! -path \*/jd1/\* \! -path \*/jd2/\* \! -path \*/ten/\* | sed -E s/^src\\/\|\\/[^\\/]+$$//g | grep -v \\.java$$ | sort | uniq | tr / .)

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
ifeq ($(origin JAVACFLAGS), undefined)
	$(eval JAVACFLAGS = $(shell dep/minJavaVerOpts))
endif
	javac $(JAVACFLAGS) -classpath "./bin:$(CLASSPATH)" -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc $(JAVADOCFLAGS) -classpath "./bin:$(CLASSPATH)" -sourcepath src $(PACKAGES) -d doc

Resplendence.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST.MF Resplendence.jar -C bin META-INF -C bin com/kreative -C bin glguerin
	chmod +x Resplendence.jar

Resplendence.app: Resplendence-Pre10.15.app Resplendence-MacOS10.15.app Resplendence-MacOS11.0.app

Resplendence-Pre10.15.app: dep Resplendence.jar
	mkdir -p Resplendence-Pre10.15.app/Contents/MacOS
	mkdir -p Resplendence-Pre10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo Resplendence-Pre10.15.app/Contents
	cp -f dep/Info.plist Resplendence-Pre10.15.app/Contents
	cp -f dep/universalJavaApplicationStub-Pre10.15 Resplendence-Pre10.15.app/Contents/MacOS/Resplendence
	cp -f dep/*.icns Resplendence-Pre10.15.app/Contents/Resources
	cp -f dep/*.jar Resplendence-Pre10.15.app/Contents/Resources/Java
	cp -f bin/*.dff Resplendence-Pre10.15.app/Contents/Resources/Java
	cp -f Resplendence.jar Resplendence-Pre10.15.app/Contents/Resources/Java

Resplendence-MacOS10.15.app: dep Resplendence.jar
	mkdir -p Resplendence-MacOS10.15.app/Contents/MacOS
	mkdir -p Resplendence-MacOS10.15.app/Contents/Resources/Java
	cp -f dep/PkgInfo Resplendence-MacOS10.15.app/Contents
	cp -f dep/Info.plist Resplendence-MacOS10.15.app/Contents
	cp -f dep/universalJavaApplicationStub-MacOS10.15 Resplendence-MacOS10.15.app/Contents/MacOS/Resplendence
	cp -f dep/*.icns Resplendence-MacOS10.15.app/Contents/Resources
	cp -f dep/*.jar Resplendence-MacOS10.15.app/Contents/Resources/Java
	cp -f bin/*.dff Resplendence-MacOS10.15.app/Contents/Resources/Java
	cp -f Resplendence.jar Resplendence-MacOS10.15.app/Contents/Resources/Java

Resplendence-MacOS11.0.app: dep Resplendence.jar
	mkdir -p Resplendence-MacOS11.0.app/Contents/MacOS
	mkdir -p Resplendence-MacOS11.0.app/Contents/Resources/Java
	cp -f dep/PkgInfo Resplendence-MacOS11.0.app/Contents
	cp -f dep/Info.plist Resplendence-MacOS11.0.app/Contents
	cp -f dep/universalJavaApplicationStub-MacOS11.0 Resplendence-MacOS11.0.app/Contents/MacOS/Resplendence
	cp -f dep/*.icns Resplendence-MacOS11.0.app/Contents/Resources
	cp -f dep/*.jar Resplendence-MacOS11.0.app/Contents/Resources/Java
	cp -f bin/*.dff Resplendence-MacOS11.0.app/Contents/Resources/Java
	cp -f Resplendence.jar Resplendence-MacOS11.0.app/Contents/Resources/Java

.PHONY: all eclipseall clean eclipseclean Resplendence.app
