import sys
import cv2
import numpy as np
import musicalbeeps


class foundNotes:
    note = None
    location = None
    xVal = None

def getVal(note):
    return note.xVal
def main():
    win_name = "text"
    image = cv2.imread("lamb.png")
    cNote = cv2.imread("c.png")
    dNote = cv2.imread("d.png")
    eNote = cv2.imread("e.png")
    dHalf = cv2.imread("dhalf.png")
    eHalf = cv2.imread("eHalf.png")
    cHalf = cv2.imread("cHalf.png")
    gFull = cv2.imread("gfull.png")
    gHalf = cv2.imread("ghalf.png")

    type = ["C", "D", "E", "D", "E", "C", "G", "G"]
    templateArray = [cNote, dNote, eNote, dHalf, eHalf, cHalf, gFull, gHalf]
    foundArray = []
    writer = 0
    for template in range(len(templateArray)):

        dy = templateArray[template].shape[0] / 2
        dx = templateArray[template].shape[1] / 2
        dy = int(dy)
        dx = int(dx)
        c = cv2.matchTemplate(image, templateArray[template], cv2.TM_CCOEFF_NORMED)

        found = image.copy()
        thresh = .94
        pts = np.where(c > thresh)

        for i in range(len(pts[0])):
            start_x, start_y = pts[1][i] - dx, pts[0][i] - dy
            end_x, end_y = pts[1][i] + dx, pts[0][i] + dy
            start_x = int(start_x)
            found = cv2.rectangle(found, (start_x + dx, start_y + dy), (end_x + dx, end_y + dy), (0, 0, 255), 2)
            temp = foundNotes()
            temp.note = type[template]
            temp.location = (pts[1][i], pts[0][i])
            temp.xVal = pts[1][i]
            foundArray.append(temp)
        #
        # print(foundArray)
        writer += 1
        if writer == 3:
            cv2.imwrite("fefw.png", found)
        cv2.imshow(win_name, found)
        cv2.waitKey(0)
    # for x in foundArray:
    #     print(x.location)
    # return

    adder = []
    threshold = 5

    for x in range(len(foundArray)):
        skip = True
        if x != len(foundArray) - 1:
            if abs(foundArray[x].location[0] - foundArray[x + 1].location[0]) < threshold and abs(foundArray[x].location[1] - foundArray[x + 1].location[1]) < 20:
                skip = False
        if skip:
            adder.append(foundArray[x])



    dy = templateArray[0].shape[0] / 2
    dx = templateArray[0].shape[1] / 2
    dy = int(dy)
    dx = int(dx)

    for i in adder:
        found = image.copy()
        start_x, start_y = i.location[0] - dx, i.location[1] - dy
        end_x, end_y = i.location[0] + dx, i.location[1] + dy
        start_x = int(start_x)
        found = cv2.rectangle(found, (start_x + dx, start_y + dy), (end_x + dx, end_y + dy), (0, 0, 255), 2)

        #
        # cv2.imshow(win_name, found)
        # cv2.waitKey(0)

    generalYT = 167
    generalYB = 651

    thresholdVal = 20
    topList = []
    bottomList = []
    for x in adder:
        if abs(x.location[1] - generalYT) > thresholdVal and x.location[1] < 700:
            bottomList.append(x)
        elif x.location[1] < 700:
            topList.append(x)


    topList.sort(key=getVal)
    bottomList.sort(key=getVal)
    print(len(topList))
    print(len(bottomList))

    for x in topList:
        print (x.location)



    player = musicalbeeps.Player(volume=0.01, mute_output=False)
    # loop through top list
    for x in topList:
        player.play_note(x.note, duration=.5)
    # loop through bottom list
    for x in bottomList:
        player.play_note(x.note, duration=.5)


if __name__ == '__main__':
    main()
