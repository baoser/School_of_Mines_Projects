# Bao Nguyen
import cv2
import numpy as np

def fuse_color_images(A, B):
    assert(A.ndim == 3 and B.ndim == 3)
    assert(A.shape == B.shape)
    # Allocate result image.
    C = np.zeros(A.shape, dtype=np.uint8)
    # Create masks for pixels that are not zero.
    A_mask = np.sum(A, axis=2) > 0
    B_mask = np.sum(B, axis=2) > 0
    # Compute regions of overlap.
    A_only = A_mask & ~B_mask
    B_only = B_mask & ~A_mask
    A_and_B = A_mask & B_mask
    C[A_only] = A[A_only]
    C[B_only] = B[B_only]
    C[A_and_B] = 0.5 * A[A_and_B] + 0.5 * B[A_and_B]
    return C

# names of images except for the first one
file_name_arr = ["mural02.jpg","mural03.jpg",
                 "mural04.jpg","mural05.jpg","mural06.jpg",
                 "mural07.jpg","mural08.jpg","mural09.jpg",
                 "mural10.jpg","mural11.jpg","mural12.jpg"]

#read in the first image
img1 = cv2.imread("mural01.jpg")

#choose points from the first image and destination points
src_pts = np.array([[154, 121], [380, 71], [304, 502], [27, 479]])
dst_pts = np.array([[150,400], [469, 400], [469, 836], [150, 836]])

# find homography
H, _ = cv2.findHomography(src_pts, dst_pts)

width = 6500
height = 1200
canvas = np.zeros((height, width, 3), dtype=np.uint8)

#warp first image and blank canvas
warped = cv2.warpPerspective(img1, H, (width,height))
warped = fuse_color_images(canvas, warped)
previous_img = img1

#for loop to stitch all the images together
for i in file_name_arr:
    #read 2 images and convert to gray images
    img = cv2.imread(i)
    gray_img1 = cv2.cvtColor(previous_img, cv2.COLOR_BGR2GRAY)
    gray_img2 = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    #ORB detector
    detector = cv2.ORB_create(nfeatures=2000,  # default = 500

                              nlevels=8,  # default = 8

                              firstLevel=0,  # default = 0

                              patchSize=31,  # default = 31

                              edgeThreshold=31)  # default = 31

    #matcher object
    matcher = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=False)

    #detect keypoints
    kp_gray_img1, desc_gray_img1 = detector.detectAndCompute(gray_img1, mask=None)
    kp_gray_img2, desc_gray_img2 = detector.detectAndCompute(gray_img2, mask=None)

    # Match query image descriptors to the training image.
    # Use k nearest neighbor matching and apply ratio test.
    matches = matcher.knnMatch(desc_gray_img2, desc_gray_img1, k=2)
    good = []
    for m, n in matches:
        if m.distance < 0.8 * n.distance:
            good.append(m)
    matches = good

    dst_pts = np.float32([kp_gray_img1[m.trainIdx].pt for m in matches]).reshape(
        -1, 1, 2)
    src_pts = np.float32([kp_gray_img2[m.queryIdx].pt for m in matches]).reshape(
        -1, 1, 2)
    #find homography from 2 to 1
    H1, _ = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC)
    H2 = H @ H1
    warped2 = cv2.warpPerspective(img, H2, (width,height))
    fused = fuse_color_images(warped, warped2)

    #reassign variables
    previous_img = img
    warped = fused
    H = H2

#output
cv2.imwrite("output.jpg", warped)

# Same program but for my photos
file_name_arr2 = ["my_img2.JPG",
                 "my_img3.JPG","my_img4.JPG","my_img5.JPG",
                 "my_img6.JPG"]

#read in the first image
img1 = cv2.imread("my_img1.JPG")

#choose points from the first image and destination points
src_pts = np.array([[681, 2057], [1625, 2033], [1617, 2937], [681, 3081]])
dst_pts = np.array([[150,300], [239, 300], [239, 389], [150, 389]])

# find homography
H, _ = cv2.findHomography(src_pts, dst_pts)

width = 1500
height = 700
canvas = np.zeros((height, width, 3), dtype=np.uint8)

#warp first image and blank canvas
warped = cv2.warpPerspective(img1, H, (width,height))
warped = fuse_color_images(canvas, warped)
previous_img = img1

#for loop to stitch all the images together
for i in file_name_arr2:
    #read 2 images and convert to gray images
    img = cv2.imread(i)
    gray_img1 = cv2.cvtColor(previous_img, cv2.COLOR_BGR2GRAY)
    gray_img2 = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    #ORB detector
    detector = cv2.ORB_create(nfeatures=2000,  # default = 500

                              nlevels=8,  # default = 8

                              firstLevel=0,  # default = 0

                              patchSize=31,  # default = 31

                              edgeThreshold=31)  # default = 31

    #matcher object
    matcher = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=False)

    #detect keypoints
    kp_gray_img1, desc_gray_img1 = detector.detectAndCompute(gray_img1, mask=None)
    kp_gray_img2, desc_gray_img2 = detector.detectAndCompute(gray_img2, mask=None)

    # Match query image descriptors to the training image.
    # Use k nearest neighbor matching and apply ratio test.
    matches = matcher.knnMatch(desc_gray_img2, desc_gray_img1, k=2)
    good = []
    for m, n in matches:
        if m.distance < 0.8 * n.distance:
            good.append(m)
    matches = good

    dst_pts = np.float32([kp_gray_img1[m.trainIdx].pt for m in matches]).reshape(
        -1, 1, 2)
    src_pts = np.float32([kp_gray_img2[m.queryIdx].pt for m in matches]).reshape(
        -1, 1, 2)
    #find homography from 2 to 1
    H1, _ = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC)
    H2 = H @ H1
    warped2 = cv2.warpPerspective(img, H2, (width,height))
    fused = fuse_color_images(warped, warped2)

    #reassign variables
    previous_img = img
    warped = fused
    H = H2

#output
cv2.imwrite("my_output.jpg", warped)


