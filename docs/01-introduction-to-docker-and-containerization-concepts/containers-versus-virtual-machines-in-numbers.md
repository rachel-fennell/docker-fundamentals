# Containers vs VMs

## Goal
Measure how long it takes to start and finish a container, how much disk a small and a large base image use, and compare with the boot time and disk size of a virtual machine you have used (or the published size of a cloud VM image). Present the results in a table with a short explanation of why the numbers differ.

## Steps taken
1. Ran the alpine and ubuntu container commands twice and recorded the second run time.
2. Checked each image’s disk usage and content size with docker images
3. Inspected ubuntu’s image history with docker history.
4. Compared the container results with the published Ubuntu Server 24.04 LTS ARM64 cloud image size. I did not boot the VM 

## Commands
time docker run --rm alpine:3.22 true
time docker run --rm alpine:3.22 true
time docker run --rm ubuntu:24.04 true
time docker run --rm ubuntu:24.04 true

docker images alpine:3.22
docker images ubuntu:24.04
docker history ubuntu:24.04

## Results

Alpine container
Second runtime: 0.224 s
Local disk usage: 13.4 MB
Content size: 4.22 MB 
Layers/history: not recorded

Ubuntu container
Second runtime: 0.236 s
Local disk usage: 141 MB
Content size: 30.9 MB
Layers/history: one 110 MB entry and four 0B entries

Ubuntu Server VM 
Second runtime: not measured
Local disk usage: not measured
Content size: 588 MB published image download size
Layers/history: N/A

The VM image size is from ubuntu’s official cloud image listing. I used a published cloud image size for the VM comparison and did not measure its boot time.

## Observations
The second container runs completed in under a second. The first runs took longer because Docker had to download the images. 