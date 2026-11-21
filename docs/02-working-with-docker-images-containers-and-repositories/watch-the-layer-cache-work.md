# Layer cache work

## Goal
Extend hello-web's Dockerfile with a RUN step that installs a package (for example apk add --no-cache curl). Build twice, change only index.html and build again, then move the COPY above the RUN and repeat. Record which steps were CACHED each time and the build durations. 

## Steps taken
1. Added RUN apk add --no-cache curl after FROM in the Dockerfile with COPY after RUN
2. Built the image twice without changing any files
3. Changed only index.html and built again
4. Moved COPY above RUN, changed index.html again, and built a fourth time

## Commands
time docker build --progress=plain -t hello-web:1.0.0 hello-web (4 times)

## Results
Build 1 — RUN before COPY, first build
RUN apk add --no-cache curl: DONE 0.5s
COPY index.html: DONE 0.0s
Total: 2.157s

Build 2 — unchanged rebuild
RUN apk add --no-cache curl: CACHED
COPY index.html: CACHED
Total: 0.599s

Build 3 — changed index.html, COPY last
RUN apk add --no-cache curl: CACHED
COPY index.html: DONE 0.0s
Total: 1.083s

Build 4 — changed index.html, COPY before RUN
COPY index.html: DONE 0.0s
RUN apk add --no-cache curl: DONE 0.5s
Total: 1.661s

With COPY after RUN, changing index.html rebuilt only the COPY step while the package-install step stayed cached. When COPY came before RUN, changing the page invalidated the later RUN step too, so Docker installed curl again. Shows why instructions that change less often should come before instructions that change more often

## Observations
Very easy to complete this task with clear instructions. 