
#include "rocksdbjni.h"

void buffer_copy(const void *source, size_t source_pos, void *dest, size_t dest_pos, size_t length) {
  memmove(((char *)dest)+dest_pos, ((const char *)source)+source_pos, length);
}
