package com.example.tailoringmanagement.upperbody

open class FullSleeve(
    frontLen: Float,
    backLen: Float,
    chest: Float,
    waist: Float,
    seat: Float,
    shoulderWidth: Float,
    armHole: Float,
    var sleeveLen: Float,
    var bicep: Float,
    var cuff: Float
) : UpperBody(frontLen, backLen, chest, waist, seat, shoulderWidth, armHole)
