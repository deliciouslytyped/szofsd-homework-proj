## Notes

### `-Dprism.forceGPU=true`
The machine I'm testing on seems to support OpenGL on it's X server, and it works over X11 forwarding, however,
this option needs to be bassed to JavaFX's Prism graphics engine stuff to force hardware rendering on,
because graphics cards are whitelisted or something, and JavaFX ends up disabling 3D graphics.

This option is set in `pom.xml`, as well as in Main.java for configurations where passing the argument directly may be omitted.
Since it's set in `Main.java`, anywhere else is basically redundant, but perhaps this can be improved later.

#### Finding the source of the graphics issue
https://www.google.com/search?client=firefox-b-d&q=glxgears+works+but+javafx+doesnt

https://wiki.openjdk.org/display/OpenJFX/Debug+Flags#DebugFlags-PrismVerbose

https://www.google.com/search?client=firefox-b-d&q=%22failed+graphics+hardware+qualifier+check%22
https://github.com/openjfx/javafx-maven-plugin/blob/3b0520e0d26ae6b11398f872b82088129eb4077a/src/main/java/org/openjfx/JavaFXBaseMojo.java#L143
https://maven.apache.org/guides/mini/guide-configuring-plugins.html#Mapping_Collection_Types
https://github.com/gluonhq/substrate/issues/891#issuecomment-802169710
-> https://github.com/openjdk/jfx/blob/master/modules/javafx.graphics/src/main/java/com/sun/prism/es2/X11GLFactory.java#L31
https://github.com/gluonhq/gluonfx-maven-plugin/issues/318

https://www.google.com/search?client=firefox-b-d&q=javafx+3d+platform+support
https://github.com/search?q=repo%3Aopenjdk%2Fjfx+scene3d&type=code&p=2
https://github.com/openjdk/jfx/blob/4b24c8690d01634179571d24c8919469b209bec6/modules/javafx.graphics/src/main/java/javafx/application/ConditionalFeature.java#L102
https://github.com/openjdk/jfx/blob/4b24c8690d01634179571d24c8919469b209bec6/modules/javafx.graphics/src/main/java/com/sun/javafx/tk/quantum/QuantumToolkit.java#L1224
https://github.com/openjdk/jfx/blob/4b24c8690d01634179571d24c8919469b209bec6/modules/javafx.graphics/src/main/java/com/sun/prism/es2/ES2Pipeline.java#L206
https://github.com/openjdk/jfx/blob/4b24c8690d01634179571d24c8919469b209bec6/tests/system/src/test/java/test/robot/test3d/RT35019Test.java#L57
https://www.google.com/search?client=firefox-b-d&q=java+null3d
https://github.com/jgneff/javafx-graphics/blob/master/src/javafx.graphics/classes/com/sun/prism/null3d/NULL3DPipeline.java

#### passing properties to the VM
(for figuring out how to pass the above property when using `javafx:run`)
https://www.google.com/search?q=mvn+pass+settings+to+app
https://www.google.com/search?client=firefox-b-d&q=maven+pass+properties+to+exec
https://www.google.com/search?q="javafx%3Arun"+properties
https://stackoverflow.com/questions/417152/how-do-i-set-javas-min-and-max-heap-size-through-environment-variables
https://www.google.com/search?q=configuration+systemproperties

### It's kind of difficult to embed SVG in GitHub Markdown 
Apparently it supposed to work now but the only thing I actually got to work was like:

```html
<p align="center">
  <img src="./feladat.svg" style="width: 50%;" />
</p>
```
People were saying something like `img` tags don't have XSS issues, so this is probably why this worked, but I don't know why the normal Markdown image embed didn't.
TODO: I may have accidentally just used normal link syntax?

Also it's unclear when CSS/other stuff is or isn't allowed, because I wasn't able to get `div` tags to work, but the img embed above has a width style.

https://stackoverflow.com/questions/46381436/github-svg-not-rendering-at-all/46381586#46381586
https://stackoverflow.com/questions/13808020/include-an-svg-hosted-on-github-in-markdown/16462143#16462143
https://stackoverflow.com/questions/13808020/include-an-svg-hosted-on-github-in-markdown/21521184#21521184
https://stackoverflow.com/questions/13808020/include-an-svg-hosted-on-github-in-markdown
https://www.google.com/search?client=firefox-b-d&q=github+readme+not+rendering+svg
https://github.com/isaacs/github/issues/316
https://rawgit.com/
https://www.google.com/search?client=firefox-b-d&q=github+private+repo+embed+svg
https://github.com/potherca-blog/StackOverflow/blob/master/question.13808020.include-an-svg-hosted-on-github-in-markdown/readme.md
https://github.com/orgs/community/discussions/22728
https://codinhood.com/nano/git/center-images-text-github-readme
https://pragmaticpineapple.com/adding-custom-html-and-css-to-github-readme/
https://www.google.com/search?client=firefox-b-d&q=git+markdown+css
https://github.github.com/gfm/

### Extracting SVG from PDF "image"
I ended up using `pdf2svg` to convert a page of the pdf, edited out a bunch of elements in Chrome's dev tools, 
(Firefox element highlighting in SVGs doesn't work for some reason even though this (https://bugzilla.mozilla.org/show_bug.cgi?id=1567087) issue is closed
and then used some random (https://boxy-svg.com/ , it's a bit obnoxious; you have to log in to export) SVG editor to fix the canvas and positioning.
I should have probably just used InkScape.

https://stackoverflow.com/questions/4120567/how-to-prevent-my-pdf-to-svg-conversion-code-from-generating-bloated-content
https://discourse.mozilla.org/t/better-svg-editing/12925/2

### Error around exec and compile (might have been 3. in misc)
I wasn't taking notes properly here so I don't remember what the exact issue was:
https://www.google.com/search?client=firefox-b-d&q=%22The+parameter+%27executable%27+is+missing+or+invalid%22
https://www.google.com/search?client=firefox-b-d&q=exec%3Ajava+classnotfoundexception
https://stackoverflow.com/questions/17458243/class-not-found-exception-with-exec-maven-plugin-when-run-on-linux

### `Could not find or load main class org.apache.maven.wrapper.MavenWrapperMain`
SSL certificate verification was broken on the test machine for some reason.
This resulted in `wget $QUIET "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"`, which is part of the `mvnw` script,
silently failing to download the Maven Wraper JAR.

(didn't help, issue is too obscure in hindsight) https://www.google.com/search?client=firefox-b-d&q=%22Could+not+find+or+load+main+class+org.apache.maven.wrapper.MavenWrapperMain%22
https://www.google.com/search?client=firefox-b-d&q=ubuntu+certificates+broken
https://www.google.com/search?client=firefox-b-d&q=ubuntu+reinstall+certificates
https://www.google.com/search?q=ubuntu+20.04+"Unable+to+locally+verify+the+issuer's+authority."

This is what actually fixed it:
https://askubuntu.com/questions/1075780/wget-fails-by-a-certificate-problem/1331305#1331305
`sudo ln -s  /etc/ssl/certs/ca-certificates.crt /usr/local/ssl/cert.pem`

### The IntelliJ Maven version
It's listed in the settings window, if you search maven in it.

https://www.google.com/search?client=firefox-b-d&q=intellij+maven+version

### Maven wrapper without binaries
`mvn wrapper:wrapper -Dtype=script`

NOTE: apparently the wrapper explanatory page is different from the wrapper plugin page...would be nice fof the latter also linked back to the former.
https://maven.apache.org/wrapper/
https://maven.apache.org/wrapper/maven-wrapper-plugin/usage.html

### misc
1. Get help with `help:help`, `buildplan:`
2. `help:describe -Dplugin=javafx -Dgoal=run`
3. `exec:exec` does not imply `compile` for some reason.
4. Change current git branch name with `git branch -m [oldname] newname`
5. Delete remote git branch with `git push origin -d branchname`
6. GitHub Actions usage: https://docs.github.com/en/billing/managing-billing-for-github-actions/viewing-your-github-actions-usage
7. git add all untracked files with `git add -A`
8. maven site / javadoc empirically seems to use relative paths
9. Having an ssh and a MobaXterm session open, with the latter forwarding X11 to its Xorg and the former using DISPLAY works just fine
10. cache git credentials for some time `git config --local credential.helper "cache --timeout=$((3600*4))"`
