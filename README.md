# SDCardStressTester

Background
==========
Many Android phones use an microSD card for extra storage since it is a much cheaper option to larger internal storage sizes. Since important files such as photos and documents will be constantly saved to the microSD, there is always a fear of data loss of corruption. Fortunately microSD cards utilize wear leveling algorithms to level the wear across different data blocks so that one area of blocks on the microSD card does not become prematurely worn down (which can lead to data loss). However, many microSD cards still use the filesystem to balance some of the wear, even though the card itself should handle most of it. Filesystems such as FAT, NTFS, and HFS+ are designed to aid in wear leveling, but the file system utilized by Android, ext3, has been shown not to do so. So one way to test how well the card can handle constant wear, without the aid of wear leveling, is to run a stress test. A stress test “is a form of deliberately intense or thorough testing used to determine the stability of a given system or entity.” (Wikipedia). Most consumer SD cards will be tested mainly for the wear they would receive in a device such as a camera, which would not be used everyday (if the SD card was considered for “professional” use it would likely receive more thorough testing). Since most people use their phones every day, usually for long periods of time, the cards will have to go through as much daily wear as a computer hard drive, which they are not designed to do. 


