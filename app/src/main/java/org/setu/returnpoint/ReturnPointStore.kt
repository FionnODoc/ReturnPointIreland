package org.setu.returnpoint

import java.util.concurrent.atomic.AtomicLong

class ReturnPointStore {
    private val points = ArrayList<ReturnPoint>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<ReturnPoint> {
        return points
    }

    fun findOne(id: Long): ReturnPoint? {
        return points.find { p -> p.id == id }
    }

    fun create(point: ReturnPoint) {
        points.add(point.copy(id = lastId.incrementAndGet()))
    }

    fun update(point: ReturnPoint): Boolean {
        val index = points.indexOfFirst { p -> p.id == point.id }
        if (index == -1) {
            return false
        }
        points[index] = point
        return true
    }

    fun delete(id: Long): Boolean {
        return points.removeAll { p -> p.id == id }
    }
}
