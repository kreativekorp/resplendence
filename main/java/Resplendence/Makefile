SRCFILES := $(shell find src -name \*.java \! -path \*/jd1/\* \! -path \*/jd2/\* \! -path \*/ten/\*)
RESFILES := $(shell (find src -name \*.png ; find src -name \*.wav ; find src -name \*.dff ; find src -name \*.jnilib) | sed s/^src\\///)
PACKAGES := $(shell find src/com/kreative -type d \! -path \*/.\* | sed s/^src\\/// | tr / .)

all: clean bin doc Resplendence.app

eclipseall: eclipseclean Resplendence.app

clean:
	rm -rf bin
	rm -rf doc
	rm -rf Resplendence.app

eclipseclean:
	rm -rf Resplendence.app

bin: src
	mkdir -p bin
	javac -classpath ../KSFL/bin:../OpenXION/bin:../PowerPaint/bin:dep/orange-extensions.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc -classpath ../KSFL/bin:../OpenXION/bin:../PowerPaint/bin:dep/orange-extensions.jar -sourcepath src $(PACKAGES) -d doc

Resplendence.app: dep bin
	mkdir -p Resplendence.app/Contents/MacOS
	mkdir -p Resplendence.app/Contents/Resources/Java/com/kreative
	cp -f dep/PkgInfo Resplendence.app/Contents
	cp -f dep/Info.plist Resplendence.app/Contents
	cp -f dep/universalJavaApplicationStub Resplendence.app/Contents/MacOS/Resplendence
	cp -f dep/*.icns Resplendence.app/Contents/Resources
	cp -R "../KSFL/bin/com/kreative/"* Resplendence.app/Contents/Resources/Java/com/kreative/
	cp -R "../OpenXION/bin/com/kreative/"* Resplendence.app/Contents/Resources/Java/com/kreative/
	cp -R "../PowerPaint/bin/com/kreative/"* Resplendence.app/Contents/Resources/Java/com/kreative/
	cp -R bin/com/kreative/* Resplendence.app/Contents/Resources/Java/com/kreative/
	cp -R bin/glguerin Resplendence.app/Contents/Resources/Java/
	cp -f bin/*.dff Resplendence.app/Contents/Resources/Java/

.PHONY: all eclipseall clean eclipseclean
