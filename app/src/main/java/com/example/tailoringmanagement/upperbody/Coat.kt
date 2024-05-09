package com.example.tailoringmanagement.upperbody

class Coat(
    frontLen: Float,
    backLen: Float,
    chest: Float,
    waist: Float,
    seat: Float,
    shoulderWidth: Float,
    armHole: Float,
    sleeveLen: Float,
    bicep: Float,
    cuff: Float,
    var individualShoulder: Float,
    var firstButtonPos: Float,
    var halfBack: Float
) : FullSleeve(frontLen, backLen, chest, waist, seat, shoulderWidth, armHole, sleeveLen, bicep, cuff)
